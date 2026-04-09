package kr.go.ap.core.repository.primary.mapper.resource.eqmnt;


import kr.go.ap.core.primary.nia.dto.resource.eqmnt.EqmntDto;
import kr.go.ap.core.primary.nia.dto.resource.eqmnt.EqmntSipcDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface EqmntMapper {
    List<EqmntDto> selectEquipList() throws PersistenceException;
    List<EqmntSipcDto> selectEquipSipcList() throws PersistenceException;
    void insertEquipSipc(HashMap<String, Object> map) throws PersistenceException;
    void deleteEquipSipc(String tid) throws PersistenceException;
}
