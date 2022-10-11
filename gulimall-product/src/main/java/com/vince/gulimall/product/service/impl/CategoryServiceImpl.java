package com.vince.gulimall.product.service.impl;

import com.vince.gulimall.product.service.CategoryBrandRelationService;
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

import com.vince.gulimall.product.dao.CategoryDao;
import com.vince.gulimall.product.entity.CategoryEntity;
import com.vince.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> ansList=entities.stream().filter(item->item.getParentCid()==0).map(item->{
            item.setChildren(getChildren(item,entities));
            return item;
        }).sorted((item1,item2)-> (item1.getSort()==null?0:item1.getSort())-(item2.getSort()==null?0:item2.getSort())).collect(Collectors.toList());
        return ansList;
    }

    /**
     * 查询分类路径，样式[2,34,225]
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path=new ArrayList<>();
        findParentPath(path,catelogId);
        System.out.println(path);
        return path.toArray(new Long[path.size()]);
    }

    /**
     * 修改分类名时，同步修改关联表的分类名
     * @param category
     */
    @Override
    @Transactional
    public void updateByIdDetail(CategoryEntity category) {

        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    private void findParentPath(List<Long> path, Long catelogId) {
        CategoryEntity categoryEntity = this.getById(catelogId);
        //有父分类时遍历
        if(categoryEntity.getParentCid()!=0)findParentPath(path,categoryEntity.getParentCid());
        path.add(catelogId);
    }

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> entities) {
        List<CategoryEntity> ansList=entities.stream().filter(item->item.getParentCid()==root.getCatId()).map(item->{
            item.setChildren(getChildren(item,entities));
            return item;
        }).sorted((item1,item2)-> (item1.getSort()==null?0:item1.getSort())-(item2.getSort()==null?0:item2.getSort())).collect(Collectors.toList());
        return ansList;
    }

}