package com.vince.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vince.gulimall.product.entity.BrandEntity;
import com.vince.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@SpringBootTest
class GulimallProductApplicationTests {

//    @Autowired
//    BrandService brandService;

    @Test
    public void testBrand(){
        List<String> list = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        //forEach终结后，原list不变，处理后的list遍历输出
        list=list.stream().sorted((item1,item2)->{
            return -1;
        }).collect(Collectors.toList());
        System.out.println(list);
    }

}
