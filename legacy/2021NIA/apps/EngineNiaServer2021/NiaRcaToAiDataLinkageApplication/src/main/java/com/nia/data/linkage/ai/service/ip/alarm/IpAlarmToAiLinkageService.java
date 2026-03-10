package com.nia.data.linkage.ai.service.ip.alarm;

import java.io.File;

public interface IpAlarmToAiLinkageService {

    void sendAlarmData();
    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
