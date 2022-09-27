package com.vince.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

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


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> entities) {
        List<CategoryEntity> ansList=entities.stream().filter(item->item.getParentCid()==root.getCatId()).map(item->{
            item.setChildren(getChildren(item,entities));
            return item;
        }).sorted((item1,item2)-> (item1.getSort()==null?0:item1.getSort())-(item2.getSort()==null?0:item2.getSort())).collect(Collectors.toList());
        return ansList;
    }

}