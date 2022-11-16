package com.nia.data.linkage.ai.vo.server;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;


@Component
@Data
@Scope(value = "prototype")
public class NttEventDataVo implements Serializable {
	private String strresip;
	private String strresname;
	private String strsIp;
	private String strsPort;
	private String strdIp;
	private String strdPort;
	private String strsMac;
	private String strdMac;
	private String strprotocol;
	private String stripv4tos;
	private String strchannel;
	private String strsenderip;
	private String strinInterface;
	private String stroutInterface;
	private String strbytesCol;
	private String strcounts;
	private Timestamp dateregdate;
}