package com.nia.data.linkage.ip.equip.service.impl;

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
import java.util.List;

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

        ArrayList<CvnmsResourceIfVo> cvnmsResourceIfVoList = new ArrayList<>();
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            cvnmsResourceIfVoList = linkageAlarmMapper.selectCvnmsResourceIfList();

            if (cvnmsResourceIfVoList != null && cvnmsResourceIfVoList.size() > 0) {
                LOGGER.info("==========>[CvnmsResourceIfService] getCvnmsResourceIfData size(" + cvnmsResourceIfVoList.size() + ") <==============");
                niaAlarmMapper.deleteCvnmsResourceIf();

                int batchSize = 100;
                // 나눠 insert 하지 않으면 에러 / foreach문 때문인듯.
                for (int i = 0; i < cvnmsResourceIfVoList.size(); i += batchSize) {
                    List<CvnmsResourceIfVo> batchList = cvnmsResourceIfVoList.subList(i, Math.min(i + batchSize,
                            cvnmsResourceIfVoList.size()));

                    objectHashMap = new HashMap<>();
                    objectHashMap.put("cvnmsResourceIfVoList", batchList);
                    niaAlarmMapper.insertCvnmsResourceIf(objectHashMap);

                }
                strHashMap = new HashMap<>();
                strHashMap.put("key", "ipResourceIfKey");
                strHashMap.put("value", cvnmsResourceIfVoList.size() + "");
                niaAlarmMapper.updateLinkageHist(strHashMap);

                niaAlarmMapper.fcResourceIfYd();
                niaAlarmMapper.fcCreateUserOrganYd(); // 이용기관 연동

                LOGGER.info("==========>[CvnmsResourceIfService] getCvnmsResourceIfData Success! <==============");
            }
        } catch (Exception e) {
            LOGGER.error("=====> [CvnmsResourceIfService] getCvnmsResourceIfData error() " + ExceptionUtils.getStackTrace(e) + "<=====");
        }
    }
}
