package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageInterfaceMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaInterfaceMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.service.InterfaceService;
import com.nia.ip.sdn.linkage.vo.InterfaceVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("InterfaceService")
public class InterfaceServiceImpl implements InterfaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceServiceImpl.class);

    @Autowired
    private LinkageInterfaceMapper linkageInterfaceMapper;

    @Autowired
    private NiaInterfaceMapper niaInterfaceMapper;
    @Autowired
    private NiaLinkageHistMapper niaLinkageHistMapper;

    @Override
    public void getInterfaceData() {
        LOGGER.info("==========>[InterfaceService] getInterfaceData <==============");
        int insertCnt = 0;

        ArrayList<InterfaceVo> interfaceVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            interfaceVoList = linkageInterfaceMapper.selectInterfaceList();

            if(interfaceVoList != null && interfaceVoList.size() > 0) {
                LOGGER.info("==========>[InterfaceService] getInterfaceData size("+interfaceVoList.size()+") <==============");

                niaInterfaceMapper.deleteInterfaceYd();

                objectHashMap = new HashMap<>();
                objectHashMap.put("interfaceVoList", interfaceVoList);
                insertCnt = niaInterfaceMapper.insertInterfaceYd(objectHashMap);

                if(insertCnt > 0){
                    niaInterfaceMapper.deleteInterface();
                    niaInterfaceMapper.insertInterface();

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipSdnInterfaceKey");
                    strHashMap.put("value", interfaceVoList.size() +"");
                    niaLinkageHistMapper.updateLinkageHist(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [InterfaceService] getInterfaceData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
