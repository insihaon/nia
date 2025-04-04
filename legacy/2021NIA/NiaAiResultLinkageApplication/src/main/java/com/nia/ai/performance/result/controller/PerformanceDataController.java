package com.nia.ai.performance.result.controller;

import com.nia.ai.performance.result.amqp.PerformanceSendPrdAmqp;
import com.nia.ai.performance.result.mapper.PerformanceMapper;
import com.nia.ai.performance.result.vo.performance.PerformanceListVo;
import com.nia.ai.performance.result.common.UtlCommon;
import com.nia.ai.performance.result.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class PerformanceDataController {
    private static final Logger LOGGER = Logger.getLogger(PerformanceDataController.class);

	@Autowired
	private PerformanceSendPrdAmqp performanceSendPrdAmqp;

	@Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PerformanceListVo> performanceListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ResultVo> resultVoObjectFactory;

    @RequestMapping(value = "performanceResult", method = RequestMethod.POST)
    public String rowPerformanceData(@RequestBody String performanceData) {
        LOGGER.info("=====> [PerformanceDataController] rowPerformanceData : " + performanceData + " <=====");

        Object obj;
        String resultMsg = null;
        PerformanceListVo performanceListVo;
        List<LinkedHashMap<String, String>> performanceList;
        ResultVo resultVo;
        HashMap<String, Object> parameterObjMap = null;
        String octTime = null;

        try {
//            performaceListVo = performanceListVoObjectFactory.getObject();
//            obj = UtlCommon.jsonToObject(performaceListVo, performaceData);
//            performaceListVo = (PerformaceListVo)obj;

            if(StringUtils.isNotEmpty(performanceData)){
                performanceList = new ArrayList<LinkedHashMap<String, String>>();
                obj = UtlCommon.jsonToObject(performanceList, performanceData);
                performanceList = (ArrayList<LinkedHashMap<String, String>>)obj;

                resultVo = resultVoObjectFactory.getObject();
                resultVo.setResultCode("1");
                resultMsg = UtlCommon.objectToJson(resultVo);

                if(performanceList != null && performanceList.size() > 0){
                    parameterObjMap = new HashMap<String, Object>();
        //            parameterObjMap.put("roadmPerformanceList", performaceListVo.getData());
                    parameterObjMap.put("roadmPerformanceList", performanceList);
                    performanceMapper.insertPerformanceData(parameterObjMap);

                    LOGGER.info("=====> [PerformanceDataController] rowPerformanceData  performanceList()" + performanceList.size() + ") <=====");
                    octTime = performanceList.get(0).get("ocrtime");
                    LOGGER.info("=====> [PerformanceDataController] rowPerformanceData octTime : " + octTime + ") <=====");
                  //  performanceSendPrdAmqp.sendMessageCmd(performaceListVo.getData().get(0).getOcrtime()+"");
                    performanceSendPrdAmqp.sendMessageCmd(octTime);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerformanceDataController] rowPerformanceData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            LOGGER.error("=====> [PerformanceDataController] rowPerformanceData error performanceData :  "+ performanceData+ "<=====");
        }
        return resultMsg;
    }
}
