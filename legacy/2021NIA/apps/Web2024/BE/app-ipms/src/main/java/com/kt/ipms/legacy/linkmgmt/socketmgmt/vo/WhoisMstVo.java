package com.kt.ipms.legacy.linkmgmt.socketmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

@JsonRootName(value="whois")
public class WhoisMstVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -4976598980888973210L;
	
	private String query;
	
	private String queryType;
	
	private String registry;
	
	private String countryCode;
	
	private BigInteger nipAssignMstSeq;
	
	private LanguageSectionVo korean;
	
	private LanguageSectionVo english;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public LanguageSectionVo getKorean() {
		return korean;
	}

	public void setKorean(LanguageSectionVo korean) {
		this.korean = korean;
	}

	public LanguageSectionVo getEnglish() {
		return english;
	}

	public void setEnglish(LanguageSectionVo english) {
		this.english = english;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}
	
	

}
