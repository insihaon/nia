package com.nia.engine.vo.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.engine.vo.TopologyObject;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Administrator
 *
 */
@Component
@Data
@ToString
public class ProfileVo implements Serializable {

	private String profileTitle;
	private String autoRecovery;

}
