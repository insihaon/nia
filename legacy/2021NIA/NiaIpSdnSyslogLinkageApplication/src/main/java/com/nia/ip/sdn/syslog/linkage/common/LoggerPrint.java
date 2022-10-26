package com.nia.ip.sdn.syslog.linkage.common;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class LoggerPrint {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggerPrint.class);

    public static void infoLog(String logMsg){
        LOGGER.info(">>>>>>>>>>["+getClassName()+"] " + getMethodName() + "\n" + logMsg + " <<<<<<<<<<<<<<<<<");
    }

    public static void errorLog(Exception e){
        LOGGER.error(">>>>>>>>>>["+getClassName()+"] " + getMethodName() + " error : \n" + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
    }

    public static void errorLog(String msg, Exception e){
        LOGGER.error(">>>>>>>>>>["+getClassName()+"] " + getMethodName() + " " + msg +"\n error : \n" + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
    }

    public String getClassName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    public String getMethodName() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }
}
