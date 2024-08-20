package com.kt.ipms.legacy.opermgmt.limitmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbAuditDhcpBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -3150172849093656464L;

	private Integer ndhcpMaxPeakValue;

	private Integer ndhcpMinPeakValue;

	private Integer ndhcpMinIpBlockCntOver;

	private Integer ndhcpMinIpCntOver;

	private Integer ndhcpRangeDays;

	private String scomment;


	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNdhcpMaxPeakValue(Integer ndhcpMaxPeakValue) {
		this.ndhcpMaxPeakValue = ndhcpMaxPeakValue;
	}

	public Integer getNdhcpMaxPeakValue() {
		return ndhcpMaxPeakValue;
	}

	public void setNdhcpMinPeakValue(Integer ndhcpMinPeakValue) {
		this.ndhcpMinPeakValue = ndhcpMinPeakValue;
	}

	public Integer getNdhcpMinPeakValue() {
		return ndhcpMinPeakValue;
	}

	public void setNdhcpMinIpBlockCntOver(Integer ndhcpMinIpBlockCntOver) {
		this.ndhcpMinIpBlockCntOver = ndhcpMinIpBlockCntOver;
	}

	public Integer getNdhcpMinIpBlockCntOver() {
		return ndhcpMinIpBlockCntOver;
	}

	public void setNdhcpMinIpCntOver(Integer ndhcpMinIpCntOver) {
		this.ndhcpMinIpCntOver = ndhcpMinIpCntOver;
	}

	public Integer getNdhcpMinIpCntOver() {
		return ndhcpMinIpCntOver;
	}

	public void setNdhcpRangeDays(Integer ndhcpRangeDays) {
		this.ndhcpRangeDays = ndhcpRangeDays;
	}

	public Integer getNdhcpRangeDays() {
		return ndhcpRangeDays;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}