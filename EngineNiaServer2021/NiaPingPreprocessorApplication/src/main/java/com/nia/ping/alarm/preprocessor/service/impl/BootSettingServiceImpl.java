package com.nia.ping.alarm.preprocessor.service.impl;

import com.nia.ping.alarm.preprocessor.data.DataShareBean;
import com.nia.ping.alarm.preprocessor.service.BootSettingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

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
    }
}
