package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageNodeMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaNodeMapper;
import com.nia.ip.sdn.linkage.service.NodeService;
import com.nia.ip.sdn.linkage.vo.NodeVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("NodeService")
public class NodeServiceImpl implements NodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private LinkageNodeMapper linkageNodeMapper;

    @Autowired
    private NiaNodeMapper niaNodeMapper;

    @Autowired
    private NiaLinkageHistMapper niaLinkageHistMapper;

    @Override
    public void getNodeData() {
        LOGGER.info("==========>[NodeService] getNodeData <==============");
        int insertCnt = 0;

        ArrayList<NodeVo> nodeVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            nodeVoList = linkageNodeMapper.selectNodeList();

            if(nodeVoList != null && nodeVoList.size() > 0) {
                LOGGER.info("==========>[NodeService] getNodeData size("+nodeVoList.size()+") <==============");

                niaNodeMapper.deleteNodeYd();

                objectHashMap = new HashMap<>();
                objectHashMap.put("nodeVoList", nodeVoList);
                insertCnt = niaNodeMapper.insertNodeYd(objectHashMap);

                if(insertCnt > 0){
                    niaNodeMapper.deleteNode();
                    niaNodeMapper.insertNode();

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipSdnNodeKey");
                    strHashMap.put("value", nodeVoList.size() +"");
                    niaLinkageHistMapper.updateLinkageHist(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [NodeService] getNodeData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
