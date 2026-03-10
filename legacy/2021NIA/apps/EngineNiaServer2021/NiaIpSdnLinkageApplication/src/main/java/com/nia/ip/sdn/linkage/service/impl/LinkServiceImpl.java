package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageLinkMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.service.LinkService;
import com.nia.ip.sdn.linkage.vo.LinkVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("LinkService")
public class LinkServiceImpl implements LinkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);

    @Autowired
    private LinkageLinkMapper linkageLinkMapper;

    @Autowired
    private NiaLinkMapper niaLinkMapper;
    @Autowired
    private NiaLinkageHistMapper niaLinkageHistMapper;

    @Override
    public void getLinkData() {
        LOGGER.info("==========>[LinkService] getLinkData <==============");
        int insertCnt = 0;

        ArrayList<LinkVo> linkVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;

        try {
            linkVoList = linkageLinkMapper.selectLinkList();

            if(linkVoList != null && linkVoList.size() > 0) {
                LOGGER.info("==========>[LinkService] getLinkData size("+linkVoList.size()+") <==============");

                niaLinkMapper.deleteLinkYd();

                objectHashMap = new HashMap<>();
                objectHashMap.put("linkVoList", linkVoList);
                insertCnt = niaLinkMapper.insertLinkYd(objectHashMap);

                if(insertCnt > 0){
                    niaLinkMapper.deleteLink();
                    niaLinkMapper.insertLink();

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "ipSdnLinkKey");
                    strHashMap.put("value", linkVoList.size() +"");
                    niaLinkageHistMapper.updateLinkageHist(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [LinkService] getLinkData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
