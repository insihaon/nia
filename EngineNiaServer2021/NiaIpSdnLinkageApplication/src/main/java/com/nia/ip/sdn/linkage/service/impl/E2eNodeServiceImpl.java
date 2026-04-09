package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageE2eNodeMapper;
import com.nia.ip.sdn.linkage.mapper.linkage.LinkageNodeMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaE2eNodeMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaNodeMapper;
import com.nia.ip.sdn.linkage.service.E2eNodeService;
import com.nia.ip.sdn.linkage.service.NodeService;
import com.nia.ip.sdn.linkage.vo.E2eNodeVo;
import com.nia.ip.sdn.linkage.vo.NodeVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("E2eNodeService")
public class E2eNodeServiceImpl implements E2eNodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(E2eNodeServiceImpl.class);

    @Autowired
    private LinkageE2eNodeMapper linkageE2eNodeMapper;

    @Autowired
    private NiaE2eNodeMapper niaE2eNodeMapper;
    @Autowired
    private NiaLinkageHistMapper niaLinkageHistMapper;

    @Override
    public void getE2eNodeData() {
        LOGGER.info("==========>[E2eNodeService] getE2eNodeData <==============");
        int insertCnt = 0;

        ArrayList<E2eNodeVo> e2eNodeVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            e2eNodeVoList = linkageE2eNodeMapper.selectE2eNodeList();

            if(e2eNodeVoList != null && e2eNodeVoList.size() > 0) {
                LOGGER.info("==========>[E2eNodeService] getE2eNodeData size("+e2eNodeVoList.size()+") <==============");

                niaE2eNodeMapper.deleteE2eNodeYd();

                objectHashMap = new HashMap<>();
                objectHashMap.put("e2eNodeVoList", e2eNodeVoList);
                insertCnt = niaE2eNodeMapper.insertE2eNodeYd(objectHashMap);

                if(insertCnt > 0){
                    niaE2eNodeMapper.deleteE2eNode();
                    niaE2eNodeMapper.insertE2eNode();

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipSdnE2eNodeKey");
                    strHashMap.put("value", e2eNodeVoList.size() +"");
                    niaLinkageHistMapper.updateLinkageHist(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [E2eNodeService] getE2eNodeData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
