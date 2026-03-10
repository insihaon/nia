package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbOrgBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8124708738203536347L;

	private String sktOrgId;

	private String sorgNm;
	
	private String sFullOrgNm;

	private String sorgTelNo;

	private String sorgFaxNo;

	private String supKtOrgId;
	
	private String supKtOrgNm;

	private String shqOrgId;
	
	private String shqOrgNm;

	private String sorgLayerNo;

	private String sorgPrefOdrg;

	private String sorgOrgtnOdrg;

	private String sprsnlOrgTypeCd;

	private String sorgBcomTypeCd;

	private String sorgRspUserId;
	
	private String sorgRspUserNm;

	private String sorgUseYn;

	private String svirtlOrgYn;

	private String sipmsOrgYn;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSktOrgId(String sktOrgId) {
		this.sktOrgId = sktOrgId;
	}

	public String getSktOrgId() {
		return sktOrgId;
	}

	public void setSorgNm(String sorgNm) {
		this.sorgNm = sorgNm;
	}

	public String getSorgNm() {
		return sorgNm;
	}
	
	
	public String getsFullOrgNm() {
		return sFullOrgNm;
	}

	public void setsFullOrgNm(String sFullOrgNm) {
		this.sFullOrgNm = sFullOrgNm;
	}

	public void setSorgTelNo(String sorgTelNo) {
		this.sorgTelNo = sorgTelNo;
	}

	public String getSorgTelNo() {
		return sorgTelNo;
	}

	public void setSorgFaxNo(String sorgFaxNo) {
		this.sorgFaxNo = sorgFaxNo;
	}

	public String getSorgFaxNo() {
		return sorgFaxNo;
	}

	public void setSupKtOrgId(String supKtOrgId) {
		this.supKtOrgId = supKtOrgId;
	}

	public String getSupKtOrgId() {
		return supKtOrgId;
	}

	public void setShqOrgId(String shqOrgId) {
		this.shqOrgId = shqOrgId;
	}

	public String getShqOrgId() {
		return shqOrgId;
	}

	public void setSorgLayerNo(String sorgLayerNo) {
		this.sorgLayerNo = sorgLayerNo;
	}

	public String getSorgLayerNo() {
		return sorgLayerNo;
	}

	public void setSorgPrefOdrg(String sorgPrefOdrg) {
		this.sorgPrefOdrg = sorgPrefOdrg;
	}

	public String getSorgPrefOdrg() {
		return sorgPrefOdrg;
	}

	public void setSorgOrgtnOdrg(String sorgOrgtnOdrg) {
		this.sorgOrgtnOdrg = sorgOrgtnOdrg;
	}

	public String getSorgOrgtnOdrg() {
		return sorgOrgtnOdrg;
	}

	public void setSprsnlOrgTypeCd(String sprsnlOrgTypeCd) {
		this.sprsnlOrgTypeCd = sprsnlOrgTypeCd;
	}

	public String getSprsnlOrgTypeCd() {
		return sprsnlOrgTypeCd;
	}

	public void setSorgBcomTypeCd(String sorgBcomTypeCd) {
		this.sorgBcomTypeCd = sorgBcomTypeCd;
	}

	public String getSorgBcomTypeCd() {
		return sorgBcomTypeCd;
	}

	public void setSorgRspUserId(String sorgRspUserId) {
		this.sorgRspUserId = sorgRspUserId;
	}

	public String getSorgRspUserId() {
		return sorgRspUserId;
	}

	public void setSorgUseYn(String sorgUseYn) {
		this.sorgUseYn = sorgUseYn;
	}

	public String getSorgUseYn() {
		return sorgUseYn;
	}

	public void setSvirtlOrgYn(String svirtlOrgYn) {
		this.svirtlOrgYn = svirtlOrgYn;
	}

	public String getSvirtlOrgYn() {
		return svirtlOrgYn;
	}

	public void setSipmsOrgYn(String sipmsOrgYn) {
		this.sipmsOrgYn = sipmsOrgYn;
	}

	public String getSipmsOrgYn() {
		return sipmsOrgYn;
	}

	public String getSupKtOrgNm() {
		return supKtOrgNm;
	}

	public void setSupKtOrgNm(String supKtOrgNm) {
		this.supKtOrgNm = supKtOrgNm;
	}

	public String getShqOrgNm() {
		return shqOrgNm;
	}

	public void setShqOrgNm(String shqOrgNm) {
		this.shqOrgNm = shqOrgNm;
	}

	public String getSorgRspUserNm() {
		return sorgRspUserNm;
	}

	public void setSorgRspUserNm(String sorgRspUserNm) {
		this.sorgRspUserNm = sorgRspUserNm;
	}
	
	

	/** MEMBER METHOD DECLARATION END **/
}