package com.nia.ai.traffic.preprocessor.service.impl;

import com.nia.ai.traffic.preprocessor.service.AttAiTcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaTrafficPreprocessorSchdulerServiceImpl {

    @Autowired
    @Qualifier("AttAiTcaService")
    private AttAiTcaService attAiTcaService;

    @Scheduled(cron = "0 0/5 * * * *")
    public void attAiTcaTrafficHdlProcessor() {
        attAiTcaService.attAiTcaTrafficHdlProcessor();
    }
}
