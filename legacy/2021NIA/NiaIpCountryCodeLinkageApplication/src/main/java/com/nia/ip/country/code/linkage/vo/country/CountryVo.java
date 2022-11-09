package com.nia.ip.country.code.linkage.vo.country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.*;
import java.sql.Timestamp;
import java.io.Serializable;

@Data
@Component
@Scope(value = "prototype")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryVo implements Serializable {

	private String ipAddr;
	private String countryCode;
	private String registry;
	private Timestamp insertTime;
}
