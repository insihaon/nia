package kr.go.ap.core.repository.primary.mapper.linkage.pm;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.HashMap;

@Mapper
public interface PmLinkageMapper {
    void callPmPrepro(String ocrTime) throws PersistenceException;
    void insertPmData(HashMap<String, Object> map) throws PersistenceException;
}
