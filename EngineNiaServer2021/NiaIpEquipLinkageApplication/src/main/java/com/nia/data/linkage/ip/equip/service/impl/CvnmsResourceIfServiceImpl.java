package com.nia.data.linkage.ip.equip.service.impl;

import com.nia.data.linkage.ip.equip.common.UtlDateHelper;
import com.nia.data.linkage.ip.equip.mapper.linkage.LinkageEquipMapper;
import com.nia.data.linkage.ip.equip.mapper.nia.NiaEquipMapper;
import com.nia.data.linkage.ip.equip.service.CvnmsResourceIfService;
import com.nia.data.linkage.ip.equip.vo.equip.CvnmsResourceIfVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("CvnmsResourceIfService")
public class CvnmsResourceIfServiceImpl implements CvnmsResourceIfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CvnmsResourceIfServiceImpl.class);

    @Autowired
    private LinkageEquipMapper linkageAlarmMapper;

    @Autowired
    private NiaEquipMapper niaAlarmMapper;

    @Override
    public void getCvnmsResourceIfData() {
        LOGGER.info("==========>[CvnmsResourceIfService] getCvnmsResourceIfData <==============");

        ArrayList<CvnmsResourceIfVo> cvnmsResourceIfVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            cvnmsResourceIfVoList = linkageAlarmMapper.selectCvnmsResourceIfList();

            if(cvnmsResourceIfVoList != null && cvnmsResourceIfVoList.size() > 0) {
                LOGGER.info("==========>[CvnmsResourceIfService] getCvnmsResourceIfData size("+cvnmsResourceIfVoList.size()+") <==============");

                niaAlarmMapper.deleteCvnmsResourceIf();

                objectHashMap = new HashMap<>();
                objectHashMap.put("cvnmsResourceIfVoList", cvnmsResourceIfVoList);
                niaAlarmMapper.insertCvnmsResourceIf(objectHashMap);

                strHashMap = new HashMap<>();
                strHashMap.put("key", "ipResourceIfKey");
                strHashMap.put("value", cvnmsResourceIfVoList.size() +"");
                niaAlarmMapper.updateLinkageHist(strHashMap);

                niaAlarmMapper.fcResourceIfYd();
                niaAlarmMapper.fcCreateUserOrganYd();
            }
        }catch (Exception e){
            LOGGER.error("=====> [CvnmsResourceIfService] getCvnmsResourceIfData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
