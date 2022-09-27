package com.vince.gulimall.order.dao;

import com.vince.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-23 19:00:06
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
