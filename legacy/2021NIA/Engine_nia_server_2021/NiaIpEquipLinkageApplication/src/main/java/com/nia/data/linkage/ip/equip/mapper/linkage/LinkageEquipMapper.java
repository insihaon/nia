package com.nia.data.linkage.ip.equip.mapper.linkage;

import com.nia.data.linkage.ip.equip.vo.equip.CvnmsResourceIfVo;
import com.nia.data.linkage.ip.equip.vo.equip.CvnmsResourceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface LinkageEquipMapper {
    ArrayList<CvnmsResourceVo> selectCvnmsResourceList();
    ArrayList<CvnmsResourceIfVo> selectCvnmsResourceIfList();
}
