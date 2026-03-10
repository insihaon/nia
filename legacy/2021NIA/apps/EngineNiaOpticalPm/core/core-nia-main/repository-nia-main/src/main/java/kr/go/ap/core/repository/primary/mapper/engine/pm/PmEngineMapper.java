package kr.go.ap.core.repository.primary.mapper.engine.pm;

import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PmEngineMapper {
    List<OpticalPerformanceDto> selectOpticalPerformanceList(HashMap<String, String> hashMap) throws PersistenceException;

}
