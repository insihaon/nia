package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbNewZipcodeVo extends CommonVo implements Serializable {
	
	private static final long serialVersionUID = 2072439354006729698L;
	
	private String zipcode; //우편번호
	private String sido; //시도
	private String sigungu; //시군구
	private String town; //읍면
	private String roadname; //도로명
	private String bldgmgmtno; //건물 관리 번호
	private String bldg_mainno; //건물번호 본번
	private String bldg_subno; //건물번호 부번
	private String ri; //리명
	private String legal_dong; //법정동명  
	private String dong; //행정동명
	private String jibun_bonbun; //지번본번
	private String jibun_bubun; //지번부번
	private String eaddr; //영문주소
	private String newkaddr; //신주소
	private String pastkaddr; //구주소
	private String pageType;	// 주소검색팝업 호출 jsp
	public String getEaddr() {
		return eaddr;
	}
	public void setEaddr(String eaddr) {
		this.eaddr = eaddr;
	}
	
	
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getSido() {
		return sido;
	}
	public void setSido(String sido) {
		this.sido = sido;
	}
	public String getSigungu() {
		return sigungu;
	}
	public void setSigungu(String sigungu) {
		this.sigungu = sigungu;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getRoadname() {
		return roadname;
	}
	public void setRoadname(String roadname) {
		this.roadname = roadname;
	}
	public String getBldg_mainno() {
		return bldg_mainno;
	}
	public void setBldg_mainno(String bldg_mainno) {
		this.bldg_mainno = bldg_mainno;
	}
	public String getBldg_subno() {
		return bldg_subno;
	}
	public void setBldg_subno(String bldg_subno) {
		this.bldg_subno = bldg_subno;
	}
	public String getRi() {
		return ri;
	}
	public void setRi(String ri) {
		this.ri = ri;
	}
	public String getLegal_dong() {
		return legal_dong;
	}
	public void setLegal_dong(String legal_dong) {
		this.legal_dong = legal_dong;
	}
	public String getDong() {
		return dong;
	}
	public void setDong(String dong) {
		this.dong = dong;
	}
	public String getJibun_bonbun() {
		return jibun_bonbun;
	}
	public void setJibun_bonbun(String jibun_bonbun) {
		this.jibun_bonbun = jibun_bonbun;
	}
	public String getJibun_bubun() {
		return jibun_bubun;
	}
	public void setJibun_bubun(String jibun_bubun) {
		this.jibun_bubun = jibun_bubun;
	}
	public String getBldgmgmtno() {
		return bldgmgmtno;
	}
	public void setBldgmgmtno(String bldgmgmtno) {
		this.bldgmgmtno = bldgmgmtno;
	}
	public String getNewkaddr() {
		return newkaddr;
	}
	public void setNewkaddr(String newkaddr) {
		this.newkaddr = newkaddr;
	}
	public String getPastkaddr() {
		return pastkaddr;
	}
	public void setPastkaddr(String pastkaddr) {
		this.pastkaddr = pastkaddr;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
}	
