
package com.nia.alarm.simulator.vo;

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
