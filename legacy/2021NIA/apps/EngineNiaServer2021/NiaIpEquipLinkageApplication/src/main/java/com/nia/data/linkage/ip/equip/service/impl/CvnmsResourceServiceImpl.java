package com.nia.data.linkage.ip.equip.service.impl;

import com.nia.data.linkage.ip.equip.common.UtlDateHelper;
import com.nia.data.linkage.ip.equip.mapper.linkage.LinkageEquipMapper;
import com.nia.data.linkage.ip.equip.mapper.nia.NiaEquipMapper;
import com.nia.data.linkage.ip.equip.service.CvnmsResourceIfService;
import com.nia.data.linkage.ip.equip.service.CvnmsResourceService;
import com.nia.data.linkage.ip.equip.vo.equip.CvnmsResourceIfVo;
import com.nia.data.linkage.ip.equip.vo.equip.CvnmsResourceVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("CvnmsResourceService")
public class CvnmsResourceServiceImpl implements CvnmsResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CvnmsResourceServiceImpl.class);

    @Autowired
    private LinkageEquipMapper linkageAlarmMapper;

    @Autowired
    private NiaEquipMapper niaAlarmMapper;

    @Override
    public void getCvnmsResourceData() {
        LOGGER.info("==========>[CvnmsResourceService] getCvnmsResourceData <==============");

        ArrayList<CvnmsResourceVo> cvnmsResourceVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            cvnmsResourceVoList = linkageAlarmMapper.selectCvnmsResourceList();
            LOGGER.info("==========>[CvnmsResourceService] getCvnmsResourceData size("+cvnmsResourceVoList.size()+") <==============");

            if(cvnmsResourceVoList != null && cvnmsResourceVoList.size() > 0) {
                niaAlarmMapper.deleteCvnmsResource();

                objectHashMap = new HashMap<>();
                objectHashMap.put("cvnmsResourceVoList", cvnmsResourceVoList);
                niaAlarmMapper.insertCvnmsResource(objectHashMap);

                strHashMap = new HashMap<>();
                strHashMap.put("key", "ipResourceKey");
                strHashMap.put("value", cvnmsResourceVoList.size() +"");
                niaAlarmMapper.updateLinkageHist(strHashMap);

                niaAlarmMapper.fcResourceNodeYd();
                LOGGER.info("==========>[CvnmsResourceService] getCvnmsResourceData Success! <==============");
            }
        }catch (Exception e){
            LOGGER.error("=====> [CvnmsResourceService] getCvnmsResourceData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
