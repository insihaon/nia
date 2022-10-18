package com.nia.data.linkage.ai.service.ipsdn.resource;

import java.io.File;

public interface IpSdnResourceLinkageService {
    void sendNodeData();
    void sendInterfaceData();
    void sendLinkData();

    File createJsonFile(String eventType, String jsonData, String perfKey);
}
