package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbIpmsSvcSubVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 7041184856411160923L;

	private BigInteger nipmsSvcMstSeq;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNipmsSvcMstSeq(BigInteger nipmsSvcMstSeq) {
		this.nipmsSvcMstSeq = nipmsSvcMstSeq;
	}

	public BigInteger getNipmsSvcMstSeq() {
		return nipmsSvcMstSeq;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}