package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageNodeFactorMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaNodeFactorMapper;
import com.nia.ip.sdn.linkage.service.NodeFactorService;
import com.nia.ip.sdn.linkage.vo.NodeFactorVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("NodeFactorService")
public class NodeFactorServiceImpl implements NodeFactorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactorServiceImpl.class);

    @Autowired
    private LinkageNodeFactorMapper linkageNodeFactorMapper;

    @Autowired
    private NiaNodeFactorMapper niaNodeFactorMapper;
    @Autowired
    private NiaLinkageHistMapper niaLinkageHistMapper;

    @Override
    public void getNodeFactorData() {
        LOGGER.info("==========>[NodeFactorService] getNodeFactorData <==============");
        NodeFactorVo nodeFactorVo;

        ArrayList<NodeFactorVo> nodeFactorVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;
        String key;

        try {
            key = niaLinkageHistMapper.selectLinkageKey("ipSdnNodeFactorKey");

            LOGGER.info("==========>[NodeFactorService] getNodeFactorData key : "+key+" <==============");

            if(StringUtils.isNotEmpty(key)){
                nodeFactorVo = linkageNodeFactorMapper.selectMaxId();

                if(nodeFactorVo != null){
                    if(Integer.parseInt(key) < nodeFactorVo.getId()){
                        nodeFactorVoList = linkageNodeFactorMapper.selectNodeFactorList(Integer.parseInt(key));

                        if(nodeFactorVoList != null && nodeFactorVoList.size() > 0) {
                            LOGGER.info("==========>[NodeFactorService] getNodeFactorData nodeFactorVoist("+nodeFactorVoList.size() +") <==============");

                            objectHashMap = new HashMap<>();
                            objectHashMap.put("nodeFactorVoList", nodeFactorVoList);
                            niaNodeFactorMapper.insertNodeFactor(objectHashMap);

                            strHashMap = new HashMap<>();
                            strHashMap.put("key", "ipSdnNodeFactorKey");
                            strHashMap.put("value", nodeFactorVoList.get(nodeFactorVoList.size()-1).getId()+"");
                            niaLinkageHistMapper.updateLinkageHist(strHashMap);
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [NodeFactorService] getNodeFactorData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
