package com.codej.web.service;


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.ResultMap;
import com.codej.base.property.GlobalConstants;
import com.codej.base.provider.JwtTokenProvider;
import com.codej.base.utils.EncryptUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.web.mapper.db1st.DbUserMapper;
import com.codej.web.mapper.db1st.MainMapper;

@Service
public class MainService extends BaseDataService {
    @Autowired DbUserMapper userMapper;
    @Autowired MainMapper mainMapper;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired PasswordEncoder passwordEncoder;

    public Object SELECT_LOGINDATA(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>() {{
            put(GlobalConstants.Common.UID, map.get("id"));
        }};
        DbUser user = (DbUser) userMapper.SELECT_LOGIN_USER(param);
        String token = jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRolesList(), null);

        // User 정보와 토큰 정보를 반환
        HashMap<String, Object> mapUser = JsonUtil.convertObjectToMap(user);
        ResultMap data = new ResultMap();
        data.put("data", mapUser);
        data.put("accessToken", token);

        ArrayList<ResultMap> list = new ArrayList<>();
        list.add(data);

        return list;
    }

    public ResultMap SELECT_LOGIN_ID(HashMap<String, Object> map) {
        return mainMapper.SELECT_LOGIN_ID(map);
    }

    public int USER_NEW_PW(HashMap<String, Object> map) {
        String endata = (String) map.get(GlobalConstants.Common.DATA);
        HashMap data = EncryptUtil.decrypt(endata);
        data.put("password", passwordEncoder.encode((String)data.get("password")));
        return mainMapper.USER_NEW_PW(data);
    }
}
