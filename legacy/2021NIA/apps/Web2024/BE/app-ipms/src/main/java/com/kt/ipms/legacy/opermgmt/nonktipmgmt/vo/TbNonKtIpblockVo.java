package com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbNonKtIpblockVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7931673381761497003L;

	private BigInteger nnonKtIpblockSeq;
	
	private BigInteger nnonKtSvcMstSeq;

	private String sipVersionTypeCd;

	private String sipVersionTypeNm;

	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private String sipBlock;

	private Integer nbitmask;
	
	private String sflag;
	
	private String sflagTypeNm;
	
	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNnonKtIpblockSeq(BigInteger nnonKtIpblockSeq) {
		this.nnonKtIpblockSeq = nnonKtIpblockSeq;
	}

	public BigInteger getNnonKtIpblockSeq() {
		return nnonKtIpblockSeq;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeNm(String sipVersionTypeNm) {
		this.sipVersionTypeNm = sipVersionTypeNm;
	}

	public String getSipVersionTypeNm() {
		return sipVersionTypeNm;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public String getSipBlock() {
		return sipBlock;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}

	public BigInteger getNnonKtSvcMstSeq() {
		return nnonKtSvcMstSeq;
	}

	public void setNnonKtSvcMstSeq(BigInteger nnonKtSvcMstSeq) {
		this.nnonKtSvcMstSeq = nnonKtSvcMstSeq;
	}

	public String getSflag() {
		return sflag;
	}

	public void setSflag(String sflag) {
		this.sflag = sflag;
	}

	public String getSflagTypeNm() {
		return sflagTypeNm;
	}

	public void setSflagTypeNm(String sflagTypeNm) {
		this.sflagTypeNm = sflagTypeNm;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}