package com.kt.ipms.legacy.opermgmt.grantmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbAdmrApvTxnVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6549296909576813450L;

	private BigInteger nautAdmrSeq;
	
	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String scentrId;
	
	private String suserId;
	
	private String suserNm;

	private String sadmrUseYn;

	private String suserGradeCd;

	private String suserGradeNm;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNautAdmrSeq(BigInteger nautAdmrSeq) {
		this.nautAdmrSeq = nautAdmrSeq;
	}

	public BigInteger getNautAdmrSeq() {
		return nautAdmrSeq;
	}	

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}

	public void setScentrId(String scentrId) {
		this.scentrId = scentrId;
	}

	public String getScentrId() {
		return scentrId;
	}

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

	public void setSadmrUseYn(String sadmrUseYn) {
		this.sadmrUseYn = sadmrUseYn;
	}

	public String getSadmrUseYn() {
		return sadmrUseYn;
	}

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSuserGradeNm(String suserGradeNm) {
		this.suserGradeNm = suserGradeNm;
	}

	public String getSuserGradeNm() {
		return suserGradeNm;
	}

	/** MEMBER METHOD DECLARATION END **/
}