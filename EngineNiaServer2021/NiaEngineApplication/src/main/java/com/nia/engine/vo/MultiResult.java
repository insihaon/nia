package com.nia.engine.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class MultiResult implements Serializable{
	private static final long serialVersionUID = 4325332911252157171L;

	private RcaResult rcaResult;
	private List<String> multiRcaResultCodeList = new ArrayList<>();

	public void addMultiRcaResultCodeList(String string){
		multiRcaResultCodeList.add(string);
	}
}