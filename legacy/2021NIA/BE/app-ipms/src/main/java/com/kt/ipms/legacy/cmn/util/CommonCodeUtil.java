package com.kt.ipms.legacy.cmn.util;

import java.math.BigInteger;


public class CommonCodeUtil {
	public static final String IP_CREATE_TYPE_CD = "CT";
	public static final String IP_CREATE_SEQ_CD = "KP";
	public static final String IP_CREATE_SEQ_CD2 = "KP2";
	public static final String IP_VERSION_TYPE_CD = "CV";
	public static final String USE_STATUS_CD = "MS";
	public static final String USE_TYPE_CD = "MU";
	public static final String BOARD_TYPE_CD = "BH";
	public static final String BOARD_TYPE_SUB_CD = "BM";
	public static final String EXTERNAL_LINK_USE_TYPE_CD = "ET";
	public static final String HNDSET_USE_STTUS_CD = "UI";
	public static final String MENU_HIER_TYPE_CD = "UH";
	public static final String ORG_BCOM_TYPE_CD = "UE";
	public static final String PRSNL_ORG_TYPE_CD = "UO";
	public static final String SCRN_TYPE_CD = "UD";
	public static final String SCRN_URL_TYPE_CD = "UU";
	public static final String SVC_HGROUP_CD = "S1";
	public static final String SVC_MGROUP_CD = "S2";
	public static final String SVC_LGROUP_CD = "S3";
	public static final String SVC_LINE_TYPE_CD = "L0";
	public static final String SVC_USE_TYPE_CD = "SU";
	public static final String SVCNW_TYPE_CD = "UL";
	public static final String USER_CONN_RESLT_TYPE_CD = "UC";
	public static final String USER_GRADE_CD = "UR";
	public static final String USER_STTUS_CD = "US";
	public static final String USER_TYPE_CD = "UK";
	public static final String LVL_BAS_LEVEL_CD = "LL";
	public static final String ASSIGN_LEVEL_CD = "IA";
	public static final String ASSIGN_TYPE_CD = "SA";
	public static final String ASG_ASSIGN_LEVEL_CD = "SG";
	public static final String ALC_ASSIGN_LEVEL_CD = "SC";
	public static final String LVL_SUB_CD = "LS";
	public static final String SEX_LINK_USE_TYPE_CD = "CE";
	public static final String SREQUEST_ASSIGN_TYPE_CD = "AT";
	public static final String SREQUEST_AS_TYPE_CD = "RS";
	public static final String TACS_FLCT_TYPE_CD = "TA";
	
	public static final String FLCT_TYPE_CD = "FT";		// (IP주소 라우팅 비교/점검) 장비타입
	public static final String INTGRM_RSLT_CD = "RC";		// (IP주소 라우팅 비교/점검) 현행화 결과
	public static final String ROUTING_EXCEPT_CD = "EC";	// (IP주소 라우팅 비교/점검) 예외 처리
	
	public static final String IP_PRIVATE_REQ_MST_SEQ_CD = "PR";  // 사설IP신청차수
	public static final String HIST_TASK_CD = "HT";  // 이력 상세작업분류
	
	public static final String IPV4 = "CV0001";
	public static final String IPV6 = "CV0002";
	public static final int IPV4_MAX_PREFIX = 32;
	public static final int IPV6_MAX_PREFIX = 128;
	public static final int IPV4_OCTET_CNT = 4;
	public static final int IPV6_OCTET_CNT = 8;
	public static final int IPV4_BASE_BIT = 24;
	public static final int IPV6_BASE_BIT = 64;
	public static final String IPV4_MASK = "FFFFFFFF";
	public static final String IPV6_MASK = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
	public static final String NOT_AVALIABLE = "N/A";
	
	public static final String USER_GRADE_A = "UR0001";			// 시스템관리자
	public static final String USER_GRADE_S = "UR0002";			
	public static final String USER_GRADE_N = "UR0003";			// 서비스망 관리자
	public static final String USER_GRADE_C = "UR0004";			// 본부운용자
	public static final String USER_GRADE_O = "UR0005";			// 노드운용자	
	public static final String USER_GRADE_R = "UR0006";			// 조회자
	
	public static final String LOGIN_SUCCESS = "UC0001";    // 로그인 성공
	public static final String LDAP_RESULT_ID_FAIL = "UC0006";   // 계정오류 
	public static final String LDAP_RESULT_FAIL = "UC0009";  //LDAP  인증실패
	public static final String USER_PW_FAIL_CNT_OVER = "UC0016";  // 비밀번호 5회  연속 실패
	public static final String LDAP_RESULT_PWD_FAIL = "1";
	public static final String LOGIN_STTUS_RETIRE = "UC0003";  // 퇴사자 상태
	public static final String USER_HNDSET_FAIL = "UC0002";  // IP 인증실패 ( 단말변경 신청요망)
	public static final String USER_STTUS_NORMAL = "US0001";  //재직상태
	public static final String USER_STTUS_RETIRE = "US0002";   //    퇴직상태
	
	//  사용자 단말 상태코드 
	public static final String USER_HNDSET_APY = "UI0001"; //   단말 신청 상태
	public static final String USER_HNDSET_APV = "UI0002"; // 단말 승인 상태
	public static final String USER_HNDSET_REJ = "UI0003";  // 단말 반려상태
	public static final String USER_HNDSET_DET = "UI0004";  // 단말 삭제 상태
	public static final String USER_HNDSET_COLLECT = "UI0005";  // 단말 회수 상태
	
	public static final String USER_LVL_CD_NA = "000000";
	
	public static final String IPV4_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	
	public static final String IPV6_PATTERN = "(\\A([0-9a-f]{1,4}:){1,1}(:[0-9a-f]{1,4}){1,6}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,2}(:[0-9a-f]{1,4}){1,5}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,3}(:[0-9a-f]{1,4}){1,4}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,4}(:[0-9a-f]{1,4}){1,3}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,5}(:[0-9a-f]{1,4}){1,2}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,6}(:[0-9a-f]{1,4}){1,1}\\Z)|" +
			"(\\A(([0-9a-f]{1,4}:){1,7}|:):\\Z)|"+
			"(\\A:(:[0-9a-f]{1,4}){1,7}\\Z)|" +
			"(\\A((([0-9a-f]{1,4}:){6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})\\Z)|" +
			"(\\A(([0-9a-f]{1,4}:){5}[0-9a-f]{1,4}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){5}:[0-9a-f]{1,4}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,1}(:[0-9a-f]{1,4}){1,4}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,2}(:[0-9a-f]{1,4}){1,3}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,3}(:[0-9a-f]{1,4}){1,2}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|" +
			"(\\A([0-9a-f]{1,4}:){1,4}(:[0-9a-f]{1,4}){1,1}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|" +
			"(\\A(([0-9a-f]{1,4}:){1,5}|:):(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|" +
			"(\\A:(:[0-9a-f]{1,4}){1,5}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"+
			"(\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\Z)";
	
	
	public static final String SUCCESS_MSG = "SUCCESS";
	public static final String FAIL_MSG = "요청한 작업이 잘못되었습니다.";
	
	public static final String SORT_TYPE_PIP_PREFIX = "PIP_PREFIX";
	public static final String SORT_TYPE_NBITMASK = "NBITMASK";
	public static final String SORT_TYPE_DMODIFY_DT = "DMODIFY_DT";
	public static final String SORT_TYPE_SHOST_IP = "SHOST_IP";
	public static final String SORT_TYPE_SIF_NM = "SIF_NM";
	public static final String SORT_TYPE_SROUTE_GW_IF_NM = "SROUTE_GW_IF_NM";
	public static final String SORT_TYPE_SROUTE_IP_BLOCK = "SROUTE_IP_BLOCK";
	public static final String SORT_TYPE_SAUDIT_FLAG = "SAUDIT_FLAG";
	
	public static final String SORT_TYPE_IP_BLOCK_COUNT = "NIP_BLOCK_CNT"; //IPBLOCK 수
	public static final String SORT_TYPE_IP_COUNT = "NIP_CNT"; // IP수
	public static final String SORT_TYPE_MAX_PEAK = "NMAX_PEAK"; //최대할당율 (최대퍼센트)
	public static final String SORT_TYPE_MIN_PEAK = "NMIN_PEAK"; //최소할당율 (최소퍼센트)
	
	public static final String SORT_ORDR_DESC = "DESC";
	public static final String SORT_ORDR_ASC = "ASC";
	
	/** TIECKET TYPE 목록 **/
	public static final BigInteger TICKET_TYPE_IP_CREATE_SAVE = BigInteger.ONE; //생성-등록
	public static final BigInteger TICKET_TYPE_IP_CREATE_DELETE = new BigInteger("2"); //생성-삭제
	public static final BigInteger TICKET_TYPE_IP_CREATE_MODIFY = new BigInteger("3"); //생성-수정
	public static final BigInteger TICKET_TYPE_IP_ASSIGN_MERGE = new BigInteger("4"); //배정-병합
	public static final BigInteger TICKET_TYPE_IP_ASSIGN_DIVIDE = new BigInteger("5"); //배정-분할
	public static final BigInteger TICKET_TYPE_IP_ASSIGN_ASSIGN = new BigInteger("6"); //배정-배정
	public static final BigInteger TICKET_TYPE_IP_ASSIGN_RETURN = new BigInteger("7"); //배정-반납
	public static final BigInteger TICKET_TYPE_IP_ASSIGN_SERVICE = new BigInteger("8"); //배정-서비스배정
	public static final BigInteger TICKET_TYPE_IP_ASSIGN_MODIFY = new BigInteger("9"); //배정-수정
	public static final BigInteger TICKET_TYPE_IP_ALLOC_MERGE = BigInteger.TEN; //할당-병합
	public static final BigInteger TICKET_TYPE_IP_ALLOC_DIVIDE = new BigInteger("11"); //할당-분할
	public static final BigInteger TICKET_TYPE_IP_ALLOC_ALLOC = new BigInteger("12"); //할당-할당
	public static final BigInteger TICKET_TYPE_IP_ALLOC_CANCEL = new BigInteger("13"); //할당-해지
	public static final BigInteger TICKET_TYPE_IP_ALLOC_RETURN = new BigInteger("14"); //할당-반납
	public static final BigInteger TICKET_TYPE_IP_ALLOC_MODIFY = new BigInteger("15"); //할당-수정
	
	/** WHOIS 신청서 유형 코드 **/
	public static final String WHOIS_REQUEST_TYPE_NEW = "01"; // 신규 신청서
	public static final String WHOIS_REQUEST_TYPE_ADD = "02"; // 추가 신청서
	public static final String WHOIS_REQUEST_TYPE_RTN = "03"; // 삭제 신청서
	public static final String WHOIS_REQUEST_TYPE_MOD = "04"; // 변경 신청서
	public static final String WHOIS_REQUEST_TYPE_INF = "05"; // 인프라 신청서
	public static final String WHOIS_REQUEST_TYPE_CHK = "06"; // IP 체크 (재할당정보의 등록가능 여부 확인)
	public static final String WHOIS_REQUEST_TYPE_INFO = "07"; // IP 조회 (재할당정보 조회)
	
	/** WHOIS 전송 유형 코드 **/
	public static final String WHOIS_TRANS_TYPE_STANDBY = "01"; // 전송대기
	public static final String WHOIS_TRANS_TYPE_SUBMIT = "02"; // 전송
	public static final String WHOIS_TRANS_TYPE_REJECT = "03"; // 반송
	public static final String WHOIS_TRANS_TYPE_REGIST = "04"; // 등록완료
	public static final String WHOIS_TRANS_TYPE_COMPLETE = "05"; // 전송완료
	// public static final String WHOIS_TRANS_TYPE_FAIL = "06"; // 전송실패
	// public static final String WHOIS_TRANS_TYPE_EXCLUDE = "07"; // 등록제외
	
	/** WHOIS 공통 **/
	public static final String WHOIS_REQUEST_RTN_MSG = "회선해지";
	public static final String WHOIS_REQUEST_PUBLISH_ALL = "0"; // 전체공개
	public static final String WHOIS_REQUEST_PUBLISH_PART = "1"; // 부분공개
	public static final String WHOIS_REQUEST_NET_TYPE_INFRA = "INFRA";
	public static final String WHOIS_REQUEST_NET_TYPE_ETC = "ETC";
	
	/** EXCEL 공통 **/
	public static final int MAX_EXCEL_ROW_COUNT = 50000;
	public static final String EXCEL_SHEET_NAME = "ExportDataSheet";
	
	/** TRACE ROUTE 공통 **/
	public static final String TRACE_SEQ_PATTERN = "(\\d+)";
	public static final String TRACE_IPV4SEQ_PATTERN ="(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])";
	public static final String TRACE_IPV4ADDR_PATTERN = "(" + TRACE_IPV4SEQ_PATTERN + "\\.){3,3}" + TRACE_IPV4SEQ_PATTERN;		
	public static final String TRACE_ASORGCD_PATTERN1 = ".*?";
	public static final String TRACE_ASORGCD_PATTERN2 = "((?:[A][S]*[0-9]*))";
	public static final String TRACE_MAX_HOPCNT ="20";
	public static final String TRACE_TTL_LIMIT ="1.5";
	public static final String TRACE_PROTOCOL_TYPE_ICMP = "I";
	public static final String TRACE_PROTOCOL_TYPE_TCP = "T";
	
	/**SOCKET 공통 **/
	public static final String SOCKET_TYPE_LDAP = "LDAP";
	public static final String SOCKET_TYPE_WHOIS = "WHOIS";
	
	/** DB FUNCTION BATCH SIF_ID **/
	public static final String BIF_UF_BATCH_WHOIS = "IPM_IPM_BT_0006";
	public static final String BIF_UF_FILE_MAKE_N_TB_ALLOC = "IPM_NMS_BT_0002";
	public static final String BIF_UF_FILE_MAKE_N_ALLOCDATA = "IPM_OTH_BT_0002";
	
	/** WHOIS 정보변경 신청 상태 코드 **/
	public static final String WHOIS_MODIFY_REQ = "변경신청";
	public static final String WHOIS_MODIFY_APPR = "승인";
	public static final String WHOIS_MODIFY_REJECT = "반려";
	
	/** IP 현행화 관리 > DB 현행화 결과 상태 코드 **/
	public static final String INTGRM_DB_RESULT01 = "DR0001";	// IP블록 크기 변경 실패
	public static final String INTGRM_DB_RESULT02 = "DR0002";	// IP블록 크기 변경 성공
	public static final String INTGRM_DB_RESULT03 = "DR0003";	// 할당/해지 작업 실패 
	public static final String INTGRM_DB_RESULT04 = "DR0004";	// IPMS,장비 완전 일치
	
	/** 이력관리 유형 목록 **/
	public static final BigInteger HIST_TASK_SAVE = BigInteger.ONE; // 등록
	public static final BigInteger HIST_TASK_MODIFY = new BigInteger("2"); // 수정
	public static final BigInteger HIST_TASK_DELETE = new BigInteger("3"); // 삭제
	public static final BigInteger HIST_TASK_MERGE = new BigInteger("4"); // 병합
	public static final BigInteger HIST_TASK_DIVIDE = new BigInteger("5"); // 분할
	public static final BigInteger HIST_TASK_ASSIGN = new BigInteger("6"); // 배정
	public static final BigInteger HIST_TASK_RETURN = new BigInteger("7"); // 반납
	public static final BigInteger HIST_TASK_SERVICE = new BigInteger("8"); // 서비스배정
	public static final BigInteger HIST_TASK_ALLOC = new BigInteger("9"); // 할당
	public static final BigInteger HIST_TASK_CANCEL = BigInteger.TEN; // 해지
	public static final BigInteger HIST_TASK_ALLOC_RESV = new BigInteger("11"); // 할당예약
	public static final BigInteger HIST_TASK_CANCEL_RESV = new BigInteger("12"); // 할당예약해지
	
	/** 사설IP신청 > 상태 코드 **/
	public static final String REQUEST_STAT01 = "REQ0001";		// 접수
	public static final String REQUEST_STAT02 = "REQ0002";		// 승인
	public static final String REQUEST_STAT03 = "REQ0003";		// 반려
	public static final String REQUEST_STAT02_NM = "승인";		// 승인
	public static final String REQUEST_STAT03_NM = "반려";		// 반려
	
	
	/** 사설IP신청 > 신청유형 코드 **/
	public static final String REQUEST_TYPE01 = "10";		// 신규신청
	public static final String REQUEST_TYPE02 = "20";		// 삭제신청
	public static final String REQUEST_TYPE01_NM = "신규신청";		// 신규신청
	public static final String REQUEST_TYPE02_NM = "삭제신청";		// 삭제신청
	
}
