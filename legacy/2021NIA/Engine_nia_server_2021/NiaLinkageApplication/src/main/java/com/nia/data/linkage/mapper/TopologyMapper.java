package com.nia.data.linkage.mapper;

import com.nia.data.linkage.vo.topology.TsdnTopologyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TopologyMapper {
    List<TsdnTopologyVo> selectTsdnTopology();
    void insertTopology(HashMap<String, Object> map);
    void insertEquipPort(HashMap<String, Object> map);
    void insertTsdnService(HashMap<String, Object> map);
    void insertTsdnServicePce(HashMap<String, Object> map);
    void insertTsdnServiceNode(HashMap<String, Object> map);
    void deleteTopology();
    void deleteEquipPort();
    void deleteTsdnService();
    void deleteTsdnServicePce();
}
