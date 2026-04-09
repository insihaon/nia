package com.kt.ipms.legacy.opermgmt.boardmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.util.Date;
import java.math.BigInteger;


public class TbBoardVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6457488370142851113L;

	private BigInteger seq;

	private String sboardTypeCd;

	private String sboardTypeNm;

	private String sboardTypeSubCd;

	private String sboardTypeSubNm;

	private String sboardTitle;

	private String sboardContents;

	private Date dnotiStartDt;

	private Date dnotiEndDt;

	private String suseTypeCd;

	private String suseTypeNm;

	private Integer nreadCnt;
	
	private String scrnType;
	
	private Integer nreplyCnt;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public BigInteger getSeq() {
		return seq;
	}

	public void setSboardTypeCd(String sboardTypeCd) {
		this.sboardTypeCd = sboardTypeCd;
	}

	public String getSboardTypeCd() {
		return sboardTypeCd;
	}

	public void setSboardTypeNm(String sboardTypeNm) {
		this.sboardTypeNm = sboardTypeNm;
	}

	public String getSboardTypeNm() {
		return sboardTypeNm;
	}

	public void setSboardTypeSubCd(String sboardTypeSubCd) {
		this.sboardTypeSubCd = sboardTypeSubCd;
	}

	public String getSboardTypeSubCd() {
		return sboardTypeSubCd;
	}

	public void setSboardTypeSubNm(String sboardTypeSubNm) {
		this.sboardTypeSubNm = sboardTypeSubNm;
	}

	public String getSboardTypeSubNm() {
		return sboardTypeSubNm;
	}

	public void setSboardTitle(String sboardTitle) {
		this.sboardTitle = sboardTitle;
	}

	public String getSboardTitle() {
		return sboardTitle;
	}

	public void setSboardContents(String sboardContents) {
		this.sboardContents = sboardContents;
	}

	public String getSboardContents() {
		return sboardContents;
	}

	public void setDnotiStartDt(Date dnotiStartDt) {
		this.dnotiStartDt = dnotiStartDt;
	}

	public Date getDnotiStartDt() {
		return dnotiStartDt;
	}

	public void setDnotiEndDt(Date dnotiEndDt) {
		this.dnotiEndDt = dnotiEndDt;
	}

	public Date getDnotiEndDt() {
		return dnotiEndDt;
	}

	public void setSuseTypeCd(String suseTypeCd) {
		this.suseTypeCd = suseTypeCd;
	}

	public String getSuseTypeCd() {
		return suseTypeCd;
	}

	public void setSuseTypeNm(String suseTypeNm) {
		this.suseTypeNm = suseTypeNm;
	}

	public String getSuseTypeNm() {
		return suseTypeNm;
	}

	public void setNreadCnt(Integer nreadCnt) {
		this.nreadCnt = nreadCnt;
	}

	public Integer getNreadCnt() {
		return nreadCnt;
	}

	public String getScrnType() {
		return scrnType;
	}

	public void setScrnType(String scrnType) {
		this.scrnType = scrnType;
	}

	public Integer getNreplyCnt() {
		return nreplyCnt;
	}

	public void setNreplyCnt(Integer nreplyCnt) {
		this.nreplyCnt = nreplyCnt;
	}

	
	
	/** MEMBER METHOD DECLARATION END **/
}