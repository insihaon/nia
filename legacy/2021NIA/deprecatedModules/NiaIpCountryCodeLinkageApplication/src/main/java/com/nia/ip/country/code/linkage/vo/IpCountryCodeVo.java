package com.nia.ip.country.code.linkage.vo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;


@Component
@Data
@Scope(value = "prototype")
public class IpCountryCodeVo implements Serializable {
	private String ipAddr;
	private String countryCode;
	private String registry;
	private Timestamp insertTime;
}