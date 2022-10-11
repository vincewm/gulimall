package com.vince.gulimall.product;


import com.vince.gulimall.product.service.BrandService;
import com.vince.gulimall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Test
    public void testPathCatelog(){
        System.out.println(categoryService.findCatelogPath(225L));
    }

}
