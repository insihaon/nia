package com.kt.ipms.legacy.opermgmt.nodemgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class NodeMgmtVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3429128996042555054L;
	
	private BigInteger seq;
	
	private BigInteger nipAssignMstSeq;
	
	private String pipPrefix;
	
	private String beforeSsvcLineTypeCd;
	
	private String beforeSsvcLineTypeNm;
	
	private String beforeSsvcGroupCd;
	
	private String beforeSsvcGroupNm;
	
	private String beforeSsvcObjCd;
	
	private String beforeSsvcObjNm;
	
	private String afterSsvcLineTypeCd;
	
	private String afterSsvcLineTypeNm;
	
	private String afterSsvcGroupCd;
	
	private String afterSsvcGroupNm;
	
	private String afterSsvcObjCd;
	
	private String afterSsvcObjNm;
	
	private String progressStatus;
	
	private String progressStatusNm;
	
	private String sUserNm;
	
	private Date dCreateDt;
	
	private Date dCompleteDt;
	
	private String sCreateId;
	
	private String sModifyId;
	
	private String sassignLevelNm;
	
	private String sassignLevelCd;
	
	private String sassignTypeNm;
	
	private String sassignTypeCd;
	
	private String sipCreateTypeNm;
	
	private String sipCreateTypeCd;
	
	private String sipVersionTypeCd;
	
	private String sCommentType;
	
	private String sComment;
	
	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}
	
	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getBeforeSsvcLineTypeCd() {
		return beforeSsvcLineTypeCd;
	}

	public void setBeforeSsvcLineTypeCd(String beforeSsvcLineTypeCd) {
		this.beforeSsvcLineTypeCd = beforeSsvcLineTypeCd;
	}

	public String getBeforeSsvcGroupCd() {
		return beforeSsvcGroupCd;
	}

	public void setBeforeSsvcGroupCd(String beforeSsvcGroupCd) {
		this.beforeSsvcGroupCd = beforeSsvcGroupCd;
	}

	public String getBeforeSsvcObjCd() {
		return beforeSsvcObjCd;
	}

	public void setBeforeSsvcObjCd(String beforeSsvcObjCd) {
		this.beforeSsvcObjCd = beforeSsvcObjCd;
	}

	public String getAfterSsvcLineTypeCd() {
		return afterSsvcLineTypeCd;
	}

	public void setAfterSsvcLineTypeCd(String afterSsvcLineTypeCd) {
		this.afterSsvcLineTypeCd = afterSsvcLineTypeCd;
	}

	public String getAfterSsvcGroupCd() {
		return afterSsvcGroupCd;
	}

	public void setAfterSsvcGroupCd(String afterSsvcGroupCd) {
		this.afterSsvcGroupCd = afterSsvcGroupCd;
	}

	public String getAfterSsvcObjCd() {
		return afterSsvcObjCd;
	}

	public void setAfterSsvcObjCd(String afterSsvcObjCd) {
		this.afterSsvcObjCd = afterSsvcObjCd;
	}

	public String getsUserNm() {
		return sUserNm;
	}

	public void setsUserNm(String sUserNm) {
		this.sUserNm = sUserNm;
	}

	public Date getdCreateDt() {
		return dCreateDt;
	}

	public void setdCreateDt(Date dCreateDt) {
		this.dCreateDt = dCreateDt;
	}

	public Date getdCompleteDt() {
		return dCompleteDt;
	}

	public void setdCompleteDt(Date dCompleteDt) {
		this.dCompleteDt = dCompleteDt;
	}

	public String getsCreateId() {
		return sCreateId;
	}

	public void setsCreateId(String sCreateId) {
		this.sCreateId = sCreateId;
	}

	public String getsModifyId() {
		return sModifyId;
	}

	public void setsModifyId(String sModifyId) {
		this.sModifyId = sModifyId;
	}

	public String getProgressStatus() {
		return progressStatus;
	}

	public void setProgressStatus(String progressStatus) {
		this.progressStatus = progressStatus;
	}

	public String getBeforeSsvcLineTypeNm() {
		return beforeSsvcLineTypeNm;
	}

	public void setBeforeSsvcLineTypeNm(String beforeSsvcLineTypeNm) {
		this.beforeSsvcLineTypeNm = beforeSsvcLineTypeNm;
	}

	public String getBeforeSsvcGroupNm() {
		return beforeSsvcGroupNm;
	}

	public void setBeforeSsvcGroupNm(String beforeSsvcGroupNm) {
		this.beforeSsvcGroupNm = beforeSsvcGroupNm;
	}

	public String getBeforeSsvcObjNm() {
		return beforeSsvcObjNm;
	}

	public void setBeforeSsvcObjNm(String beforeSsvcObjNm) {
		this.beforeSsvcObjNm = beforeSsvcObjNm;
	}

	public String getAfterSsvcLineTypeNm() {
		return afterSsvcLineTypeNm;
	}

	public void setAfterSsvcLineTypeNm(String afterSsvcLineTypeNm) {
		this.afterSsvcLineTypeNm = afterSsvcLineTypeNm;
	}

	public String getAfterSsvcGroupNm() {
		return afterSsvcGroupNm;
	}

	public void setAfterSsvcGroupNm(String afterSsvcGroupNm) {
		this.afterSsvcGroupNm = afterSsvcGroupNm;
	}

	public String getAfterSsvcObjNm() {
		return afterSsvcObjNm;
	}

	public void setAfterSsvcObjNm(String afterSsvcObjNm) {
		this.afterSsvcObjNm = afterSsvcObjNm;
	}

	public String getSassignLevelNm() {
		return sassignLevelNm;
	}

	public void setSassignLevelNm(String sassignLevelNm) {
		this.sassignLevelNm = sassignLevelNm;
	}

	public String getSassignLevelCd() {
		return sassignLevelCd;
	}

	public void setSassignLevelCd(String sassignLevelCd) {
		this.sassignLevelCd = sassignLevelCd;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public String getProgressStatusNm() {
		return progressStatusNm;
	}

	public void setProgressStatusNm(String progressStatusNm) {
		this.progressStatusNm = progressStatusNm;
	}

	public String getsCommentType() {
		return sCommentType;
	}

	public void setsCommentType(String sCommentType) {
		this.sCommentType = sCommentType;
	}

	public String getsComment() {
		return sComment;
	}

	public void setsComment(String sComment) {
		this.sComment = sComment;
	}
	
	
	
}
