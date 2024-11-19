package com.kt.ipms.legacy.opermgmt.requiremgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class ReqBoardVo extends CommonVo implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6945792655841162950L;
	
	private BigInteger seq;
	
	private String rboardDivision;
	
	private String rboardTitle;
	
	private Date rboardDesireDate;
	
	private String rboardPurposeRequest;
	
	private String rboardImportance;
	
	private String rboardContent;
	
	private Date rboardDcreateDt;
	
	private String rboardScreateId;
	
	private String rboardSModifyId;
	
	private String sUserNm;
	
	private String rboardActionDetail;
	
	private String rboardProgress;
	
	private Date rboardExpectedDate;
	
	private BigInteger rboardSeq;
	
	private String rboardFileSeq;
	
	private String rboardFileOriginName;
	
	private String rboardFileSaveName;
	
	private String rboardFilePath;
	
	private String rboardDownloadPath;
	
	private String rboardFileSize;

	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public String getRboardDivision() {
		return rboardDivision;
	}

	public void setRboardDivision(String rboardDivision) {
		this.rboardDivision = rboardDivision;
	}

	public String getRboardTitle() {
		return rboardTitle;
	}

	public void setRboardTitle(String rboardTitle) {
		this.rboardTitle = rboardTitle;
	}

	public Date getRboardDesireDate() {
		return rboardDesireDate;
	}

	public void setRboardDesireDate(Date rboardDesireDate) {
		this.rboardDesireDate = rboardDesireDate;
	}

	public String getRboardPurposeRequest() {
		return rboardPurposeRequest;
	}

	public void setRboardPurposeRequest(String rboardPurposeRequest) {
		this.rboardPurposeRequest = rboardPurposeRequest;
	}

	public String getRboardImportance() {
		return rboardImportance;
	}

	public void setRboardImportance(String rboardImportance) {
		this.rboardImportance = rboardImportance;
	}

	public String getRboardContent() {
		return rboardContent;
	}

	public void setRboardContent(String rboardContent) {
		this.rboardContent = rboardContent;
	}

	public Date getRboardDcreateDt() {
		return rboardDcreateDt;
	}

	public void setRboardDcreateDt(Date rboardDcreateDt) {
		this.rboardDcreateDt = rboardDcreateDt;
	}

	public String getRboardScreateId() {
		return rboardScreateId;
	}

	public void setRboardScreateId(String rboardScreateId) {
		this.rboardScreateId = rboardScreateId;
	}

	public String getRboardActionDetail() {
		return rboardActionDetail;
	}

	public void setRboardActionDetail(String rboardActionDetail) {
		this.rboardActionDetail = rboardActionDetail;
	}

	public String getRboardProgress() {
		return rboardProgress;
	}

	public void setRboardProgress(String rboardProgress) {
		this.rboardProgress = rboardProgress;
	}

	public Date getRboardExpectedDate() {
		return rboardExpectedDate;
	}

	public void setRboardExpectedDate(Date rboardExpectedDate) {
		this.rboardExpectedDate = rboardExpectedDate;
	}

	public BigInteger getRboardSeq() {
		return rboardSeq;
	}

	public void setRboardSeq(BigInteger rboardSeq) {
		this.rboardSeq = rboardSeq;
	}

	public String getRboardFileOriginName() {
		return rboardFileOriginName;
	}

	public void setRboardFileOriginName(String rboardFileOriginName) {
		this.rboardFileOriginName = rboardFileOriginName;
	}

	public String getRboardFileSaveName() {
		return rboardFileSaveName;
	}

	public void setRboardFileSaveName(String rboardFileSaveName) {
		this.rboardFileSaveName = rboardFileSaveName;
	}

	public String getRboardFilePath() {
		return rboardFilePath;
	}

	public void setRboardFilePath(String rboardFilePath) {
		this.rboardFilePath = rboardFilePath;
	}
	public String getRboardDownloadPath() {
		return rboardDownloadPath;
	}

	public void setRboardDownloadPath(String rboardDownloadPath) {
		this.rboardDownloadPath = rboardDownloadPath;
	}

	public String getRboardFileSize() {
		return rboardFileSize;
	}

	public void setRboardFileSize(String rboardFileSize) {
		this.rboardFileSize = rboardFileSize;
	}

	public String getsUserNm() {
		return sUserNm;
	}

	public void setsUserNm(String sUserNm) {
		this.sUserNm = sUserNm;
	}

	public String getRboardSModifyId() {
		return rboardSModifyId;
	}

	public void setRboardSModifyId(String rboardSModifyId) {
		this.rboardSModifyId = rboardSModifyId;
	}

	public String getRboardFileSeq() {
		return rboardFileSeq;
	}

	public void setRboardFileSeq(String rboardFileSeq) {
		this.rboardFileSeq = rboardFileSeq;
	}
	
}
