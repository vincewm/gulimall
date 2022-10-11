package com.vince.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vince.common.valid.AddGroup;
import com.vince.common.valid.UpdateGroup;
import com.vince.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vince.gulimall.product.entity.BrandEntity;
import com.vince.gulimall.product.service.BrandService;
import com.vince.common.utils.PageUtils;
import com.vince.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-22 11:36:03
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //校验有注解AddGroup的字段，基本每个字段都有，主要是必须id为null
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand) {  // BindingResult result
        brandService.save(brand);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //校验有注解UpdateGroup的字段，基本每个字段都有，主要是必须id不为null
    public R update(@RequestBody @Validated({UpdateGroup.class}) BrandEntity brand) {
        brandService.updateByIdDetail(brand);

        return R.ok();
    }

    /**
     * 仅修改状态
     * @param brand
     * @return
     */
    @RequestMapping("/update/status")
    //校验传来对象的status属性，只能是0或者1。
    public R updateStatus(@RequestBody @Validated({UpdateStatusGroup.class}) BrandEntity brand) {   //只校验状态
        brandService.updateById(brand);

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
