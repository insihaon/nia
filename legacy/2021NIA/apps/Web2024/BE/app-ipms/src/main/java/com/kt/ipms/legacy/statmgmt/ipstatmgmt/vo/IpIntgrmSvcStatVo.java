package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class IpIntgrmSvcStatVo extends IpVo {

	private static final long serialVersionUID = 8270677967713264245L;
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;
	
	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private String sassignTypeCd;

	private String sassignTypeNm;
	
	
	private TbLvlMstListVo lvlMstSeqListVo; // 계위 Seq 파라미터
	
	private List<Map<String,String>> chkSvcList;
	
	private List<Map<String,String>> chkSvcLineList;
	
	private List<CommonCodeVo> sassignTypeCds;
	
	/**
	 * 할당 유형 목록 문자열
	 */
	private String sassignTypeCdMultiStr;
	
	/**
	 * 할당 유형 List<String>
	 */
	private List<String> sassignTypeMulti;
	
	/**
	 * 서비스망 목록 문자열
	 */
	private String ssvcLineCdMultiStr;
	
	/**
	 * 서비스망 List<String>
	 */
	private List<String> ssvcLineMulti;
	
	private String sblockSizeCd;
	
	private String sblockSizeNm;
	
	private List<Map<String,String>> chkBlockSizeList;
	
	private String sblockSizeMultiStr;
	
	private List<String> sblockSizeMulti;

	private String hidDate;
	
	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
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

	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}

	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}

	public String getSsvcGroupNm() {
		return ssvcGroupNm;
	}

	public void setSsvcGroupNm(String ssvcGroupNm) {
		this.ssvcGroupNm = ssvcGroupNm;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public String getSsvcObjNm() {
		return ssvcObjNm;
	}

	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public List<CommonCodeVo> getSassignTypeCds() {
		return sassignTypeCds;
	}

	public void setSassignTypeCds(List<CommonCodeVo> sassignTypeCds) {
		this.sassignTypeCds = sassignTypeCds;
	}

	public List<Map<String,String>> getChkSvcList() {
		return chkSvcList;
	}

	public void setChkSvcList(List<Map<String,String>> chkSvcList) {
		this.chkSvcList = chkSvcList;
	}

	public String getSassignTypeCdMultiStr() {
		return sassignTypeCdMultiStr;
	}

	public void setSassignTypeCdMultiStr(String sassignTypeCdMultiStr) {
		this.sassignTypeCdMultiStr = sassignTypeCdMultiStr;
	}

	public List<String> getSassignTypeMulti() {
		return sassignTypeMulti;
	}

	public void setSassignTypeMulti(List<String> sassignTypeMulti) {
		this.sassignTypeMulti = sassignTypeMulti;
	}

	public String getHidDate() {
		return hidDate;
	}

	public void setHidDate(String hidDate) {
		this.hidDate = hidDate;
	}

	public List<Map<String,String>> getChkSvcLineList() {
		return chkSvcLineList;
	}

	public void setChkSvcLineList(List<Map<String,String>> chkSvcLineList) {
		this.chkSvcLineList = chkSvcLineList;
	}

	public String getSsvcLineCdMultiStr() {
		return ssvcLineCdMultiStr;
	}

	public void setSsvcLineCdMultiStr(String ssvcLineCdMultiStr) {
		this.ssvcLineCdMultiStr = ssvcLineCdMultiStr;
	}

	public List<String> getSsvcLineMulti() {
		return ssvcLineMulti;
	}

	public void setSsvcLineMulti(List<String> ssvcLineMulti) {
		this.ssvcLineMulti = ssvcLineMulti;
	}

	public String getSblockSizeCd() {
		return sblockSizeCd;
	}

	public void setSblockSizeCd(String sblockSizeCd) {
		this.sblockSizeCd = sblockSizeCd;
	}

	public String getSblockSizeNm() {
		return sblockSizeNm;
	}

	public void setSblockSizeNm(String sblockSizeNm) {
		this.sblockSizeNm = sblockSizeNm;
	}

	public String getSblockSizeMultiStr() {
		return sblockSizeMultiStr;
	}

	public void setSblockSizeMultiStr(String sblockSizeMultiStr) {
		this.sblockSizeMultiStr = sblockSizeMultiStr;
	}

	public List<String> getSblockSizeMulti() {
		return sblockSizeMulti;
	}

	public void setSblockSizeMulti(List<String> sblockSizeMulti) {
		this.sblockSizeMulti = sblockSizeMulti;
	}

	public List<Map<String,String>> getChkBlockSizeList() {
		return chkBlockSizeList;
	}

	public void setChkBlockSizeList(List<Map<String,String>> chkBlockSizeList) {
		this.chkBlockSizeList = chkBlockSizeList;
	}

}
