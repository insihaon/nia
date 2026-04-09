package kr.go.ap.core.repository.primary.mapper.prepro.pm;

import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PmPreproMapper {
    List<OpticalPerformanceDto> selectOpticalPerformanceList(HashMap<String, String> hashMap) throws PersistenceException;
    void insertCheckOpticalPerformanceList(HashMap<String, Object> hashMap) throws PersistenceException;
}
