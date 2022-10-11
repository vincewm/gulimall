package com.vince.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vince.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.vince.gulimall.product.entity.AttrEntity;
import com.vince.gulimall.product.service.AttrAttrgroupRelationService;
import com.vince.gulimall.product.service.AttrService;
import com.vince.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.vince.gulimall.product.entity.AttrGroupEntity;
import com.vince.gulimall.product.service.AttrGroupService;
import com.vince.common.utils.PageUtils;
import com.vince.common.utils.R;



/**
 * 属性分组
 *
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-22 11:36:03
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("categoryId") Long categoryId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,categoryId);

        return R.ok().put("page", page);
    }
    /**
     * 获取分组关联的所有属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> data = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", data);
    }

    /**
     * 查询当前属性分组未关联的属性
     * @param attrgroupId
     * @param params
     * @return
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId, @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
//        查询结果要添加分类路径字段，样式[2,34,225]
        Long[] catelogPath=categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 保存关联关系
     * @param attrAttrgroupRelationEntities
     * @return
     */
    @PostMapping("/attr/relation")
    public R attrRelation(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities){
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntities);
        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 删除关联关系
     * @param attrAttrgroupRelationEntities
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R attrRelationDelete(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities){
        attrService.deleteRelation(attrAttrgroupRelationEntities);
        return R.ok();
    }


}
