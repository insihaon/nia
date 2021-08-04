package com.nia.alarm.preprocessor.service.impl;

import com.nia.alarm.preprocessor.common.NiaCodeInfo;
import com.nia.alarm.preprocessor.data.DataShareBean;
import com.nia.alarm.preprocessor.service.BootSettingService;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service("BootSettingService")
public class BootSettingServiceImpl implements BootSettingService {

    private final static Logger LOGGER = Logger.getLogger(BootSettingServiceImpl.class);

    @Autowired
    private DataShareBean dataShareBean;

    @Override
    public void init() throws Exception {
        createDataShareBean();
    }

    @Override
    public void createDataShareBean() throws Exception {
        LOGGER.info("==========>[BootSettingService] createDataShareBean <==============");
        dataShareBean.putData(NiaCodeInfo.DATA_SHARE_NAME_ALARM_QUE, new ConcurrentLinkedQueue<BasicAlarmVo>());
    }
}
