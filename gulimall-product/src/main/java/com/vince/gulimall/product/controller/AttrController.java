package com.vince.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.vince.gulimall.product.vo.AttrRespVo;
import com.vince.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.vince.gulimall.product.entity.AttrEntity;
import com.vince.gulimall.product.service.AttrService;
import com.vince.common.utils.PageUtils;
import com.vince.common.utils.R;



/**
 * 商品属性
 *
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-22 11:36:03
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catelogId}")

    public R list(@RequestParam Map<String, Object> params,@PathVariable Long catelogId,@PathVariable String attrType){
        PageUtils page = attrService.queryPage(params,catelogId,attrType);

        return R.ok().put("page", page);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.saveAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateAttr(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
