package com.nia.rca.cluster.preprocessor.service;

public interface BootSettingService {
    void init() throws Exception;
    void createDataShareBean() throws Exception;
    void startThread() throws  Exception;

}
