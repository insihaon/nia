package com.nia.ip.sdn.linkage.mapper.nia;

import com.nia.ip.sdn.linkage.vo.InterfaceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Mapper
public interface NiaInterfaceMapper {
    int insertInterfaceYd(HashMap<String, Object> hashMap);
    void insertInterface();
    void deleteInterface();
    void deleteInterfaceYd();
}
