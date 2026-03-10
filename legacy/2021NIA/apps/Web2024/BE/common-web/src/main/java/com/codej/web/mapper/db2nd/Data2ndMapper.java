package com.codej.web.mapper.db2nd;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codej.base.dto.model.ResultMap;
import com.codej.web.mapper.db1st.BaseDataMapper;

@Mapper
public interface Data2ndMapper extends BaseDataMapper {
    public ResultMap SELECT_NOW2(HashMap<String, Object> map);
    public List<ResultMap> selectCpDailyTrafficList(HashMap<String, Object> map);
    public List<ResultMap> selectCpDailyCircuitTrafficList(HashMap<String, Object> map);
    public List<ResultMap> selectCpMonthlyTrafficList(HashMap<String, Object> map);
    public List<ResultMap> selectCpMonthlyCircuitTrafficList(HashMap<String, Object> map);
    public List<ResultMap> selectIdcMonthlyCircuitTrafficList(HashMap<String, Object> map);
    public List<ResultMap> selectBackDailyTrafficStat(HashMap<String, Object> map);
    public List<ResultMap> selectBackMonthlyTrafficStat(HashMap<String, Object> map);
    public List<ResultMap> selectCpInfomation(HashMap<String, Object> map);
    public List<ResultMap> selectCpCircuit(HashMap<String, Object> map);
    public List<ResultMap> selectIdcCircuitInfomation(HashMap<String, Object> map);
    public List<ResultMap> selectIdcCenterName(HashMap<String, Object> map);
    public List<ResultMap> selectModalContentProvider(HashMap<String, Object> map);
    public List<ResultMap> selectModalInterface(HashMap<String, Object> map);
    // public List<ResultMap> selectNeCatYdProcess(HashMap<String, Object> map);
    // public int mergeNeCatYdProcess(HashMap<String, Object> map);
    public List<ResultMap> selectCpOverlapCheck(HashMap<String, Object> map);
    public int updateCpInfoData(HashMap<String, Object> map);
    public int insertCpInfoData(HashMap<String, Object> map);
    public int updateCpCircuitInfoData(HashMap<String, Object> map);
    public int insertCpCircuitInfoData(HashMap<String, Object> map);
    public int updateIdcCircuitInfoData(HashMap<String, Object> map);
    public long deleteCpInfoData(HashMap<String, Object> map);
    public long deleteCpCircuitInfoData(HashMap<String, Object> map);
    // public int insertEquipModelMst(HashMap<String, Object> param);
    public long deleteIdcCircuitInfoData(HashMap<String, Object> map);
}
