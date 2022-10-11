package com.vince.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.vince.common.constant.ProductConstant;
import com.vince.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.vince.gulimall.product.dao.AttrGroupDao;
import com.vince.gulimall.product.dao.CategoryDao;
import com.vince.gulimall.product.entity.*;
import com.vince.gulimall.product.service.CategoryService;
import com.vince.gulimall.product.vo.AttrRespVo;
import com.vince.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vince.common.utils.PageUtils;
import com.vince.common.utils.Query;

import com.vince.gulimall.product.dao.AttrDao;
import com.vince.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
@Slf4j
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrGroupDao attrGroupDao;

    /**
     * 分页查询规格属性、销售属性
     * @param params
     * @param catelogId
     * @param attrType
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params,Long catelogId,String attrType) {
        //模糊查询

        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        //判断是规格参数还是销售属性,[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
        wrapper.eq(AttrEntity::getAttrType,"base".equalsIgnoreCase(attrType)? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() :ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(StringUtils.isNotEmpty(key)){
            wrapper.and(obj->obj.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName,key));
        }
        //catelogId查询
        if(catelogId!=0) wrapper.eq(AttrEntity::getCatelogId,catelogId);
            //查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        //分页数据中加入当前属性的“所属分类”和“所属分组”
        List<AttrEntity> attrEntities = page.getRecords();
        List<AttrRespVo> attrRespVos = attrEntities.stream().map(item->{
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(item,attrRespVo);
            //查询“所属分类”和“所属分组”的name
            Long catelogId2 = attrRespVo.getCatelogId();
            Long attrGroupId=null;
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                    new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId,attrRespVo.getAttrId())
            );
            if(attrAttrgroupRelationEntity!=null) attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId2);
            //没查到的对象就不能getName了，必须防止空指针异常，习惯习惯
            if(categoryEntity!=null) attrRespVo.setCatelogName(categoryEntity.getName());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
            if(attrGroupEntity!=null) attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        //返回分页工具对象
        return pageUtils;
    }

    /**
     * 查询分组关联的所有属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(
                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId)
        );
        if(attrAttrgroupRelationEntities==null||attrAttrgroupRelationEntities.size()==0) return null;
        else {
            List<Long> attrEntitiesIds=attrAttrgroupRelationEntities.stream().map(item->item.getAttrId()).collect(Collectors.toList());
            List<AttrEntity> attrEntities=this.listByIds(attrEntitiesIds);
            return attrEntities;
        }

    }

    /**
     * 查询当前属性分组未关联的属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属分类里面的所有属性
        //先查询出当前分组所属的分类
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1当前分类下的所有分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIds = attrGroupEntities.stream().map(attrGroupEntity1 -> {
            return attrGroupEntity1.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2这些分组关联的属性
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> attrIds = relationEntities.stream().map((relationEntity) -> {
            return relationEntity.getAttrId();
        }).collect(Collectors.toList());
        // 从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0){
            wrapper.notIn("attr_id", attrIds);
        }
        //模糊查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }
    /**
     * 查询属性详情，用于修改时回显，主要回显分类路径和所属分组名
     * @param attrId
     * @return
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity,attrRespVo);
        //设置所属分类路径
        attrRespVo.setCatelogPath(categoryService.findCatelogPath(attrRespVo.getCatelogId()));
        //设置所属分组
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId,attrId)
        );
        //坑点，空指针异常
        if(attrAttrgroupRelationEntity!=null) {
            Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
            attrRespVo.setAttrGroupId(attrGroupId);
        }

        return attrRespVo;
    }

    /**
     * 保存基本规格参数和关联关系
     * @param attrVo
     */
    @Override
    @Transactional
    public void saveAttr(AttrVo attrVo) {
        this.save(attrVo);
        //仅在规格属性的分组有值时，保存属性和属性分组关联关系
        if(attrVo.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()&&attrVo.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }


    }
    /**
     * 修改属性时，同步修改与属性分组关联关系
     * @param attrVo
     */
    @Override
    @Transactional
    public void updateAttr(AttrVo attrVo) {
        //先修改属性
        this.updateById(attrVo);
        Long attrGroupId = attrVo.getAttrGroupId();
        Long attrId = attrVo.getAttrId();
        //再判断修改关联关系。如果是销售属性或者分组id是空时，不用修改
        if(attrVo.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()||attrGroupId==null){
            //todo。待优化，如果是将规格属性改为销售属性，查询删除原关联关系
            return;
        }
        //查询关联表中是否已有此属性
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId)
        );
        if(attrAttrgroupRelationEntity!=null){
//            已存在，修改
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            attrAttrgroupRelationDao.updateById(attrAttrgroupRelationEntity);
        }else {
            //不存在，新增关联关系
            attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            attrAttrgroupRelationEntity.setAttrId(attrId);
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    /**
     * 删除关联关系.根据包含属性id和分组id的list
     * @param attrAttrgroupRelationEntities
     */
    @Override
    public void deleteRelation(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities) {
        //遍历查删
        attrAttrgroupRelationEntities.stream().forEach(item->{
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                    new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, item.getAttrId()).eq(AttrAttrgroupRelationEntity::getAttrGroupId, item.getAttrGroupId())
            );
            if(attrAttrgroupRelationEntity!=null) attrAttrgroupRelationDao.deleteById(attrAttrgroupRelationEntity.getId());
        });
    }



}