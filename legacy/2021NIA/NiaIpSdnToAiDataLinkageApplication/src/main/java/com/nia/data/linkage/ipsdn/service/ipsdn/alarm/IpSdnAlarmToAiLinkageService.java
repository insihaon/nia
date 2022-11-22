package com.nia.data.linkage.ipsdn.service.ipsdn.alarm;

import java.io.File;

public interface IpSdnAlarmToAiLinkageService {
    void sendAlarmData();

    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
