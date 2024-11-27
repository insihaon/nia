package com.kt.ipms.legacy.cmn.vo;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.kt.ipms.legacy.cmn.typehandler.JsonDateSerializer;


public class CommonVo extends BaseVo {

	/** 검색시작일 */
	private String searchBgnDe = "";

	/** 검색조건 */
	private String searchCnd = "";

	/** 검색종료일 */
	private String searchEndDe = "";

	/** 검색단어 */
	private String searchWrd = "";
	
	/** 정렬타입 */
	private String sortType = "";

	/** 정렬순서(DESC,ASC) */
	private String sortOrdr = "";

	/** 검색사용여부 */
	private String searchUseYn = "";
	
	/** 검색 시작 코드 **/
	private String searchBgnCd = "";
	
	/** 검색 종료 코드 **/
	private String searchEndCd = "";

	/** 현재페이지 */
	private int pageIndex = 1;

	/** 페이지갯수 */
	private int pageUnit = 0;

	/** 페이지사이즈 */
	private int pageSize = 0;

	/** 첫페이지 인덱스 */
	private int firstIndex = 1;

	/** 마지막페이지 인덱스 */
	private int lastIndex = 10;

	/** 페이지당 레코드 개수 */
	private int recordCountPerPage = 10;

	/** 레코드 번호 */
	private int rowNo = 0;

	/** 구분 유형 */
	private String typeFlag = "";
	
	/** 등록일자 **/
	private Date dcreateDt; 
	
	/** 등록자 **/
	private String screateId;
	
	/** 등록자명 **/
	private String screateNm;
	
	/** 수정일자 **/
	private Date dmodifyDt;
	
	/** 수정자 **/
	private String smodifyId;
	
	/** 수정자명 **/ 
	private String smodifyNm;
	
	/** 공통 처리 메세지 **/
	private String commonMsg;
	
	/** 등록자 이메일 **/
	private String screateEmail;
	
	/** 수정자 이메일 **/
	private String smodifyEmail;
	
	/** 검색단어(페이지이동간) */
	private String moveType = "";
	
	/** 검색단어(페이지이동간) */
	private String moveSearchWrd = "";
	
	/** IP_버전_유형코드 (페이지이동간)**/ 
	private String moveSipVersionTypeCd;

	private String url;
	
	/**
	 * Asgn - IP 배정관리
	 * Aloc  - IP 할당관리
	 * Rout  - IP주소 라우팅 비교/점검 
	 * NodeReq - IP주소 노드 이전 신청
	 */
	private String menuType;
	
	private Map<String, Object> paramMap;

	public String getSearchBgnDe() {
		return searchBgnDe;
	}

	public void setSearchBgnDe(String searchBgnDe) {
		this.searchBgnDe = searchBgnDe;
	}

	public String getSearchCnd() {
		return searchCnd;
	}

	public void setSearchCnd(String searchCnd) {
		this.searchCnd = searchCnd;
	}

	public String getSearchEndDe() {
		return searchEndDe;
	}

	public void setSearchEndDe(String searchEndDe) {
		this.searchEndDe = searchEndDe;
	}

	public String getSearchWrd() {
		return searchWrd;
	}

	public void setSearchWrd(String searchWrd) {
		this.searchWrd = searchWrd;
	}
	
	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortOrdr() {
		return sortOrdr;
	}

	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	public String getSearchUseYn() {
		return searchUseYn;
	}

	public void setSearchUseYn(String searchUseYn) {
		this.searchUseYn = searchUseYn;
	}
	
	public String getSearchBgnCd() {
		return searchBgnCd;
	}

	public void setSearchBgnCd(String searchBgnCd) {
		this.searchBgnCd = searchBgnCd;
	}

	public String getSearchEndCd() {
		return searchEndCd;
	}

	public void setSearchEndCd(String searchEndCd) {
		this.searchEndCd = searchEndCd;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public String getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(String typeFlag) {
		this.typeFlag = typeFlag;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getDcreateDt() {
		return dcreateDt;
	}

	public void setDcreateDt(Date dcreateDt) {
		this.dcreateDt = dcreateDt;
	}

	public String getScreateId() {
		return screateId;
	}

	public void setScreateId(String screateId) {
		this.screateId = screateId;
	}

	public String getScreateNm() {
		return screateNm;
	}

	public void setScreateNm(String screateNm) {
		this.screateNm = screateNm;
	}
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getDmodifyDt() {
		return dmodifyDt;
	}

	public void setDmodifyDt(Date dmodifyDt) {
		this.dmodifyDt = dmodifyDt;
	}

	public String getSmodifyId() {
		return smodifyId;
	}

	public void setSmodifyId(String smodifyId) {
		this.smodifyId = smodifyId;
	}

	public String getSmodifyNm() {
		return smodifyNm;
	}

	public void setSmodifyNm(String smodifyNm) {
		this.smodifyNm = smodifyNm;
	}

	public String getCommonMsg() {
		return commonMsg;
	}

	public void setCommonMsg(String commonMsg) {
		this.commonMsg = commonMsg;
	}

	public String getScreateEmail() {
		return screateEmail;
	}

	public void setScreateEmail(String screateEmail) {
		this.screateEmail = screateEmail;
	}

	public String getSmodifyEmail() {
		return smodifyEmail;
	}

	public void setSmodifyEmail(String smodifyEmail) {
		this.smodifyEmail = smodifyEmail;
	}

	public String getMoveSearchWrd() {
		return moveSearchWrd;
	}

	public String getMoveType() {
		return moveType;
	}

	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}

	public void setMoveSearchWrd(String moveSearchWrd) {
		this.moveSearchWrd = moveSearchWrd;
	}
	
	public String getMoveSipVersionTypeCd() {
		return moveSipVersionTypeCd;
	}

	public void setMoveSipVersionTypeCd(String moveSipVersionTypeCd) {
		this.moveSipVersionTypeCd = moveSipVersionTypeCd;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
