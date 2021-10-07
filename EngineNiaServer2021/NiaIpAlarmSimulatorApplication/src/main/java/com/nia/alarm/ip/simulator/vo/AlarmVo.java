
package com.nia.alarm.ip.simulator.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Component()
@Data
@Scope(value = "prototype")
public class AlarmVo implements Serializable {
    private int intErrIdx;
    private int moduleSrl;
    private String strResID;
    private String strIfID;
    private String strType;
    private String strLevel;
    private String strTitle;
    private String strContent;
    private String strGroupID;
    private String strGroupID1;
    private String strGroupID2;
    private String strGroupID3;
    private String strGroupID4;
    private String strGroupID5;
    private String strGroupID6;
    private String strGroupID7;
    private String strGroupID8;
    private String strGroupID9;
    private String strGroupID10;
    private String dateRaiseDate;
    private String dateClearDate;
    private String dateRegDate;
    private String strClearCheck;
    private String strMemo;
    private String strAcknownCheck;
    private int strAcknownCheckMemberSrl;
}
