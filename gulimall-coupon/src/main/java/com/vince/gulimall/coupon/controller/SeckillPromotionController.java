package com.vince.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vince.gulimall.coupon.entity.SeckillPromotionEntity;
import com.vince.gulimall.coupon.service.SeckillPromotionService;
import com.vince.common.utils.PageUtils;
import com.vince.common.utils.R;



/**
 * 秒杀活动
 *
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-23 18:00:44
 */
@RestController
@RequestMapping("coupon/seckillpromotion")
public class SeckillPromotionController {
    @Autowired
    private SeckillPromotionService seckillPromotionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:seckillpromotion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillPromotionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:seckillpromotion:info")
    public R info(@PathVariable("id") Long id){
		SeckillPromotionEntity seckillPromotion = seckillPromotionService.getById(id);

        return R.ok().put("seckillPromotion", seckillPromotion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:seckillpromotion:save")
    public R save(@RequestBody SeckillPromotionEntity seckillPromotion){
		seckillPromotionService.save(seckillPromotion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:seckillpromotion:update")
    public R update(@RequestBody SeckillPromotionEntity seckillPromotion){
		seckillPromotionService.updateById(seckillPromotion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:seckillpromotion:delete")
    public R delete(@RequestBody Long[] ids){
		seckillPromotionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
