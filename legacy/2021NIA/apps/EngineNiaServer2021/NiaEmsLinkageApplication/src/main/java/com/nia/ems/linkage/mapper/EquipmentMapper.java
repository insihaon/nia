package com.nia.ems.linkage.mapper;

import com.nia.ems.linkage.vo.equipment.EquipInfoVo;
import com.nia.ems.linkage.vo.equipment.EquipSipcVo;
import com.nia.ems.linkage.vo.equipment.EquipVo;
import com.nia.ems.linkage.vo.performance.PerformaceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface EquipmentMapper {
    List<EquipVo> selectEquipList();
    ArrayList<EquipInfoVo> selectEquipUpdateList();
    List<EquipSipcVo> selectEquipSipcList();
    void insertEquipSipc(HashMap<String, Object> map);
    void insertEquipSlot(HashMap<String, Object> map);
    void insertEquipOpcYd(HashMap<String, Object> map);
    void insertEquipOpc();
    void insertEquipNetWorkYd(HashMap<String, Object> map);
    void insertEquipNetWork();
    void insertEquipMstYd(EquipInfoVo equipInfoVo);
    int insertNniTopologyTmp();
    void insertNniTopology();
    void insertUniTopology();
    int insertUniTopologyTmp();
    void updateEquipMst(EquipInfoVo equipInfoVo);
    void deleteEquipSipc(HashMap<String, String> map);
    void deleteEquipOpc();
    void deleteEquipOpcYd();
    void deleteEquipNetWork();
    void deleteEquipNetWorkYd();
    void deleteNniTopologyTmp();
    void deleteNniTopology();
    void deleteUniTopology();
    void deleteUniTopologyTmp();
    void deleteEquipMstYd();
}
