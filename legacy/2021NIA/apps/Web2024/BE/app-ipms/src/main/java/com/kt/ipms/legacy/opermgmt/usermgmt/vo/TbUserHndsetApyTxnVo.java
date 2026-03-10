package com.kt.ipms.legacy.opermgmt.usermgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbUserHndsetApyTxnVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2744772244978617166L;

	private String suserId;
	
	private String suserNm;

	private Integer nhndsetApySeq;

	private String suserHndsetId;

	private String shndsetUseSttusCd;

	private String shndsetUseSttusNm;

	private String shndsetApvUserId;
	
	private String shndsetApvUserNm;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSuserId(String suserId) {
		this.suserId = suserId;
	}

	public String getSuserId() {
		return suserId;
	}
	
	public String getSuserNm() {
		return suserNm;
	}

	public void setSuserNm(String suserNm) {
		this.suserNm = suserNm;
	}

	public void setNhndsetApySeq(Integer nhndsetApySeq) {
		this.nhndsetApySeq = nhndsetApySeq;
	}

	public Integer getNhndsetApySeq() {
		return nhndsetApySeq;
	}

	public void setSuserHndsetId(String suserHndsetId) {
		this.suserHndsetId = suserHndsetId;
	}

	public String getSuserHndsetId() {
		return suserHndsetId;
	}

	public void setShndsetUseSttusCd(String shndsetUseSttusCd) {
		this.shndsetUseSttusCd = shndsetUseSttusCd;
	}

	public String getShndsetUseSttusCd() {
		return shndsetUseSttusCd;
	}

	public void setShndsetUseSttusNm(String shndsetUseSttusNm) {
		this.shndsetUseSttusNm = shndsetUseSttusNm;
	}

	public String getShndsetUseSttusNm() {
		return shndsetUseSttusNm;
	}

	public void setShndsetApvUserId(String shndsetApvUserId) {
		this.shndsetApvUserId = shndsetApvUserId;
	}

	public String getShndsetApvUserId() {
		return shndsetApvUserId;
	}
	
	public String getShndsetApvUserNm() {
		return shndsetApvUserNm;
	}

	public void setShndsetApvUserNm(String shndsetApvUserNm) {
		this.shndsetApvUserNm = shndsetApvUserNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}