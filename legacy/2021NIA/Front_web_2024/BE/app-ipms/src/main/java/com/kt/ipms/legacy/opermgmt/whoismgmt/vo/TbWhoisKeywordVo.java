package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbWhoisKeywordVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -3161184237830979006L;

	private BigInteger nwhoisKeywordSeq;

	private String suserorggb;

	private String sorgname;			// 기관명
	
	private String sreplace_cd;		// 대체기준코드
	
	private String sreplace_nm;		// 대체기준명

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNwhoisKeywordSeq(BigInteger nwhoisKeywordSeq) {
		this.nwhoisKeywordSeq = nwhoisKeywordSeq;
	}

	public BigInteger getNwhoisKeywordSeq() {
		return nwhoisKeywordSeq;
	}

	public void setSuserorggb(String suserorggb) {
		this.suserorggb = suserorggb;
	}

	public String getSuserorggb() {
		return suserorggb;
	}

	public void setSorgname(String sorgname) {
		this.sorgname = sorgname;
	}

	public String getSorgname() {
		return sorgname;
	}

	public String getSreplace_cd() {
		return sreplace_cd;
	}

	public void setSreplace_cd(String sreplace_cd) {
		this.sreplace_cd = sreplace_cd;
	}

	public String getSreplace_nm() {
		return sreplace_nm;
	}

	public void setSreplace_nm(String sreplace_nm) {
		this.sreplace_nm = sreplace_nm;
	}


	/** MEMBER METHOD DECLARATION END **/
}