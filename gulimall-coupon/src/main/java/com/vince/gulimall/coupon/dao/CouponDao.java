package com.vince.gulimall.coupon.dao;

import com.vince.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-23 18:00:44
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
