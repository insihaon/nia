package com.nia.ai.performance.send.service.impl;

import com.nia.ai.performance.send.data.DataShareBean;
import com.nia.ai.performance.send.service.BootSettingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("BootSettingService")
public class BootSettingServiceImpl implements BootSettingService {

    private final static Logger LOGGER = Logger.getLogger(BootSettingServiceImpl.class);

    @Autowired
    private DataShareBean dataShareBean;

    @Override
    public void init() {
        createDataShareBean();
    }

    @Override
    public void createDataShareBean(){
        LOGGER.info("==========>[BootSettingService] createDataShareBean <==============");
    }
}
