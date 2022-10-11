package com.vince.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.vince.gulimall.product.dao.BrandDao;
import com.vince.gulimall.product.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vince.common.utils.PageUtils;
import com.vince.common.utils.Query;

import com.vince.gulimall.product.dao.CategoryBrandRelationDao;
import com.vince.gulimall.product.entity.CategoryBrandRelationEntity;
import com.vince.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelation.setCatelogName(categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName());
        categoryBrandRelation.setBrandName(brandDao.selectById(categoryBrandRelation.getBrandId()).getName());
        this.save(categoryBrandRelation);
    }

    /**
     * 修改关联表里指定品牌id的品牌名
     * 品牌名变化后，调用此方法.
     *
     * @param brandId
     */
    @Override
    public void updateBrand(Long brandId,String brandName) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);categoryBrandRelationEntity.setBrandName(brandName);
        this.update(categoryBrandRelationEntity,new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId,brandId));
    }

    /**
     * 修改关联表里指定分类id的分类名
     * 分类名变化后，调用此方法.
     * @param catId
     * @param categoryName
     */
    @Override
    public void updateCategory(Long catId, String categoryName) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogId(catId);categoryBrandRelationEntity.setCatelogName(categoryName);
        this.update(categoryBrandRelationEntity,new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getCatelogId,catId));
    }

}