package com.kt.ipms.mapper.db2nd;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.BaseUser;
import com.codej.web.mapper.db1st.BaseUserMapper;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

@Mapper
public interface IpmsUserMapper extends BaseUserMapper {
        public TbUserBasVo SELECT_LOGIN_USER(TbUserBasVo map) throws Exception;
}
