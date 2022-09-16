package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageLinkTrafficeMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkTrafficMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.service.LinkTrafficeService;
import com.nia.ip.sdn.linkage.vo.LinkTrafficVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("LinkTrafficeService")
public class LinkTrafficeServiceImpl implements LinkTrafficeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkTrafficeServiceImpl.class);

    @Autowired
    private LinkageLinkTrafficeMapper linkageLinkTrafficeMapper;

    @Autowired
    private NiaLinkTrafficMapper niaLinkTrafficMapper;
    @Autowired
    private NiaLinkageHistMapper niaLinkageHistMapper;

    @Override
    public void getLinkTrafficeData() {
        LOGGER.info("==========>[LinkTrafficeService] getLinkTrafficeData <==============");
        LinkTrafficVo linkTrafficVo;

        ArrayList<LinkTrafficVo> linkTrafficVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;
        String key;

        try {
//            Thread.sleep(120*1000);
            key = niaLinkageHistMapper.selectLinkageKey("ipSdnTrafficeFactorKey");

            LOGGER.info("==========>[LinkTrafficeService] getLinkTrafficeData key : "+key+" <==============");

            if(StringUtils.isNotEmpty(key)){
                linkTrafficVo = linkageLinkTrafficeMapper.selectMaxId();

                if(linkTrafficVo != null){
                    if(Integer.parseInt(key) < linkTrafficVo.getId()){
                        linkTrafficVoList = linkageLinkTrafficeMapper.selectLinkTrafficVoList(Integer.parseInt(key));

                        if(linkTrafficVoList != null && linkTrafficVoList.size() > 0) {
                            LOGGER.info("==========>[LinkTrafficeService] getLinkTrafficData linkTrafficVoList("+linkTrafficVoList.size() +") <==============");

                            objectHashMap = new HashMap<>();
                            objectHashMap.put("linkTrafficVoList", linkTrafficVoList);
                            niaLinkTrafficMapper.insertLinkTraffic(objectHashMap);

                            strHashMap = new HashMap<>();
                            strHashMap.put("key", "ipSdnTrafficeFactorKey");
                            strHashMap.put("value", linkTrafficVoList.get(linkTrafficVoList.size()-1).getId()+"");
                            niaLinkageHistMapper.updateLinkageHist(strHashMap);
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [LinkTrafficeService] getLinkTrafficeData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
