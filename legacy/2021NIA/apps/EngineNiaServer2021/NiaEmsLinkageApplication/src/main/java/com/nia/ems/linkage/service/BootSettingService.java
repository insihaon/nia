package com.nia.ems.linkage.service;

public interface BootSettingService {
    void init() throws Exception;
    void createDataShareBean() throws Exception;
    void startThread() throws Exception;

}
