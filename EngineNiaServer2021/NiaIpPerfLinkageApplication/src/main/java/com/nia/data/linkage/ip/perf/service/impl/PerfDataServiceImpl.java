package com.nia.data.linkage.ip.perf.service.impl;

import com.google.common.collect.Lists;
import com.nia.data.linkage.ip.perf.common.UtlDateHelper;
import com.nia.data.linkage.ip.perf.mapper.linkage.LinkagePerfMapper;
import com.nia.data.linkage.ip.perf.mapper.nia.NiaEquipMapper;
import com.nia.data.linkage.ip.perf.mapper.nia.NiaPerfMapper;
import com.nia.data.linkage.ip.perf.service.PerfDataService;
import com.nia.data.linkage.ip.perf.vo.equip.NodeMstVo;
import com.nia.data.linkage.ip.perf.vo.perf.PerfVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("PerfDataService")
public class PerfDataServiceImpl implements PerfDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerfDataServiceImpl.class);

    @Autowired
    private LinkagePerfMapper linkagePerfMapper;

    @Autowired
    private NiaPerfMapper niaPerfMapper;

    @Autowired
    private NiaEquipMapper niaEquipMapper;

    @Override
    public void getPerfData() {
        LOGGER.info("==========>[PerfDataService] getPerfData <==============");

        String inttimestamp = null;

        ArrayList<PerfVo> perfVoList;
        ArrayList<PerfVo> insertPerfVoList;
        ArrayList<NodeMstVo> nodeMstVoList;
        HashMap<String, Object> objectHashMap;
        HashMap<String, String> strHashMap;
        List<List<PerfVo>> listByGroup = null;

        PerfVo maxPerfVo;

        try {
            inttimestamp = niaPerfMapper.selectPerfYdKey("ipPerfKey");

            if(StringUtils.isNotEmpty(inttimestamp)){

                maxPerfVo = linkagePerfMapper.selectMaxIntTimestamp();

                if(maxPerfVo != null){
                    if(Integer.parseInt(inttimestamp) < maxPerfVo.getIntTimestamp()){
                        perfVoList = linkagePerfMapper.selectPerfList(Integer.parseInt(inttimestamp));

                        if(perfVoList != null && perfVoList.size() > 0) {
                            LOGGER.info("==========>[PerfDataService] getPerfData perfVoList("+perfVoList.size() +") <==============");

                            nodeMstVoList = niaEquipMapper.selectNodeList();
                            insertPerfVoList = new ArrayList<>();

//                            for(PerfVo perfVo : perfVoList){
//                                for(NodeMstVo nodeMstVo : nodeMstVoList){
//                                    if(perfVo.getStrResID().equals(nodeMstVo.getNodeNum())){
//                                        insertPerfVoList.add(perfVo);
//                                    }
//                                }
//                            }

                            insertPerfData(perfVoList);

//                            insertPerfVoList = perfVoList;

//                            if(insertPerfVoList.size() < 50){
//                                objectHashMap = new HashMap<>();
//                                objectHashMap.put("perfVoList", insertPerfVoList);
//                                niaPerfMapper.insertPerf(objectHashMap);
//                            }else{
//                                listByGroup = Lists.partition(insertPerfVoList, insertPerfVoList.size() / 30);
//
//                                if(listByGroup.size() > 0 ) {
//                                    for (List<PerfVo> perfList : listByGroup) {
//                                        objectHashMap = new HashMap<>();
//                                        objectHashMap.put("perfVoList", perfList);
//                                        niaPerfMapper.insertPerf(objectHashMap);
//                                    }
//                                }
//                            }

                            if(perfVoList.size() > 0){
                                strHashMap = new HashMap<>();
                                strHashMap.put("key", "ipPerfKey");
                                strHashMap.put("value", perfVoList.get(perfVoList.size()-1).getIntTimestamp()+"");
                                niaPerfMapper.updatePerfYdKey(strHashMap);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerfDataService] getPerfData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void insertPerfData(ArrayList<PerfVo> list) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        int i = 1;
        int totalCycle = 0;
        List<PerfVo> tmpPerfList = null;
        HashSet<PerfVo> perfVoHashSet = null;

        try {
            if(list != null){
                tmpPerfList = new ArrayList<PerfVo>();
                perfVoHashSet = new LinkedHashSet<>();

                if(list.size() > 100){

                    if((list.size() % 100) == 0){
                        totalCycle = list.size() / 100;
                    }else{
                        totalCycle = ((int)Math.floor(list.size() / 100))+1;
                    }

                    for(PerfVo perfVo : list){
                        tmpPerfList.add(perfVo);
                        perfVoHashSet.add(perfVo);

                        if(i == totalCycle){
                            continue;
                        }else{
                            if(tmpPerfList.size() == 100){
                                map.put("perfVoList", tmpPerfList);
                                niaPerfMapper.insertPerf(map);

                                tmpPerfList.clear();
                                map.clear();
                                i++;
                            }
                        }
                    }

                    if(tmpPerfList != null && tmpPerfList.size() > 0){
                        map.put("perfVoList", tmpPerfList);
                        niaPerfMapper.insertPerf(map);
                    }
                }else{
                    map.put("perfVoList", tmpPerfList);
                    niaPerfMapper.insertPerf(map);
                }

                tmpPerfList.clear();
                map.clear();
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerfDataService] getPerfData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
