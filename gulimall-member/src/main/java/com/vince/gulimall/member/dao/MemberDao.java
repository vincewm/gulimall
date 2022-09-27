package com.vince.gulimall.member.dao;

import com.vince.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author vince
 * @email 2363659293@qq.com
 * @date 2022-09-23 18:43:22
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
