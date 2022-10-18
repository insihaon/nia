package com.nia.data.linkage.ai.service.ipsdn.factor;

import java.io.File;

public interface IpSdnFactorLinkageService {

    void sendFactorData();
    File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath);
}
