package com.vince.gulimall.product.dao;

import com.vince.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-22 11:36:03
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
