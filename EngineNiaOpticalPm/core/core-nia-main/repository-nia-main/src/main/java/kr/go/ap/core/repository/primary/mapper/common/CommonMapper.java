package kr.go.ap.core.repository.primary.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;


import java.util.HashMap;

@Mapper
public interface CommonMapper {
    void updateLinkageYdKey(HashMap<String,String> strHashMap) throws PersistenceException;
}
