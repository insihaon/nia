/*
 * NERC version 1.0
 *
 *  Copyright ⓒ 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 */
package com.nia.alarm.ip.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
@Scope(value = "prototype")
public class AlarmVo implements Serializable {
    private int intErrIdx;
    private int moduleSrl;
    private String strResID;
    private String strResName;
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
