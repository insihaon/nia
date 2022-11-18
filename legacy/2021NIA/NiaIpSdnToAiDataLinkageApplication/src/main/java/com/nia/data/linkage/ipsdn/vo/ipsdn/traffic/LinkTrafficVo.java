package com.nia.data.linkage.ipsdn.vo.ipsdn.traffic;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
@Component
@Scope(value = "prototype")
@Data
public class LinkTrafficVo implements Serializable {
	private int sdn_node_id;
	private int id;
	private String strresid;
	private String strresnm;
	private int sdn_if_id;
	private String strifid;
	private String strifnm;
	private int fltbpsin;
	private int fltbpsout;
	private int fltppsin;
	private int fltppsout;
	private String nren_id;
	private String nren_name;
	private Timestamp measured_datetime;



}