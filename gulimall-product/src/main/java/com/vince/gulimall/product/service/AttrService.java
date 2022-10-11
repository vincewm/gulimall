package com.vince.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vince.common.utils.PageUtils;
import com.vince.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.vince.gulimall.product.entity.AttrEntity;
import com.vince.gulimall.product.vo.AttrRespVo;
import com.vince.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-22 11:36:03
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params,Long catelogId,String attrType);

    void saveAttr(AttrVo attrVo);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}

