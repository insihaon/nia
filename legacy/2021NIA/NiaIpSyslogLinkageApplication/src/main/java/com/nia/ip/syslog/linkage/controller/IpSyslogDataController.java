package com.nia.ip.syslog.linkage.controller;

import com.nia.ip.syslog.linkage.common.UtlCommon;
import com.nia.ip.syslog.linkage.common.UtlDateHelper;
import com.nia.ip.syslog.linkage.mapper.SyslogMapper;
import com.nia.ip.syslog.linkage.vo.ResultVo;
import com.nia.ip.syslog.linkage.vo.syslog.SyslogListVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;

@RestController
public class IpSyslogDataController {
    private static final Logger LOGGER = Logger.getLogger(IpSyslogDataController.class);

	@Autowired
    private SyslogMapper syslogMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SyslogListVo> syslogListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ResultVo> resultVoObjectFactory;

    @RequestMapping(value = "yd001/syslog",method = RequestMethod.POST)
    public String sysLogData(@RequestBody String syslog) {
        LOGGER.info("=====> [IpSyslogDataController] sysLogData : " + syslog + " <=====");

        Object obj;
        String resultMsg = null;
        SyslogListVo syslogListVo;
        ResultVo resultVo;
        HashMap<String, Object> parameterObjMap = null;
        Timestamp currentDate;
        String ydNum;

        try {
            syslogListVo = syslogListVoObjectFactory.getObject();
            obj = UtlCommon.jsonToObject(syslogListVo, syslog);
            syslogListVo = (SyslogListVo)obj;

            resultVo = resultVoObjectFactory.getObject();
            resultVo.setRes_code(1);
            resultMsg = UtlCommon.objectToJson(resultVo);

            ydNum = syslogMapper.selectIpYbNumKey();

            currentDate = UtlDateHelper.getCurrentTime();

            syslogListVo.setInsertTime(currentDate);
            syslogListVo.setYdNum(ydNum);
            syslogMapper.insertSyslogYd(syslogListVo);

            parameterObjMap = new HashMap<String, Object>();
            parameterObjMap.put("syslogList", syslogListVo.getData());
            parameterObjMap.put("ydNum", ydNum);
            syslogMapper.insertSyslogData(parameterObjMap);

            LOGGER.info("=====> [IpSyslogDataController] sysLogData size : " + syslogListVo.getData().size() + " <=====");
        }catch (Exception e){
            LOGGER.error("=====> [IpSyslogDataController] sysLogData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            LOGGER.error("=====> [IpSyslogDataController] sysLogData error syslog :  "+ syslog+ "<=====");
        }
        return resultMsg;
    }
}
