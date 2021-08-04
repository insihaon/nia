package com.nia.ai.per.ap.service.impl;

import com.nia.ai.per.ap.common.RcaCodeInfo;
import com.nia.ai.per.ap.data.DataShareBean;
import com.nia.ai.per.ap.service.BootSettingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
