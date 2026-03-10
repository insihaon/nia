package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbSvcHgroupCdVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -635929729670626980L;

	private String ssvcHgroupCd;

	private String ssvcHgroupNm;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSsvcHgroupCd(String ssvcHgroupCd) {
		this.ssvcHgroupCd = ssvcHgroupCd;
	}

	public String getSsvcHgroupCd() {
		return ssvcHgroupCd;
	}

	public void setSsvcHgroupNm(String ssvcHgroupNm) {
		this.ssvcHgroupNm = ssvcHgroupNm;
	}

	public String getSsvcHgroupNm() {
		return ssvcHgroupNm;
	}

	/** MEMBER METHOD DECLARATION END **/
}