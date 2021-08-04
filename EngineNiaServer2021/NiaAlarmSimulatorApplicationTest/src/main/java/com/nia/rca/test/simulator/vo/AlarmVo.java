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
package com.nia.rca.test.simulator.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Administrator
 *
 */
@Data
@Component
public class AlarmVo implements Serializable {
	private String alarmno;
    private String sysname;
    private String equiptype;
    private String unit;
    private Timestamp alarmtime;
    private Timestamp receivetime;
    private String alarmloc;
    private String alarmmsg;
    private String alarmlevel;
}
