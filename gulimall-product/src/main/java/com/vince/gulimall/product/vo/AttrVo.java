package com.vince.gulimall.product.vo;


import com.vince.gulimall.product.entity.AttrEntity;
import lombok.Data;

/**
 * 规格参数vo
 *
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-22 11:36:03
 */
@Data
public class AttrVo extends AttrEntity {
    //当前属性属于的属性分组id
    private Long attrGroupId;
}


