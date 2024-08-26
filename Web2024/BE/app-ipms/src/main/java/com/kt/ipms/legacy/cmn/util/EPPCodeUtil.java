package com.kt.ipms.legacy.cmn.util;

public class EPPCodeUtil {
	
	public enum EPPSyntaxErrorCodes {
		ERR_START_IP(1101, "시작 주소가 IPv4주소 문법에 맞지 않습니다.")
		, ERR_END_IP(1102, "끝 주소가 IPv4주소 문법에 맞지 않습니다.")
		, ERR_IP_BLOCK(1103, "주소블럭 문법이 적절하지 않습니다.")
		, ERR_KOR_ORG_NAME(1104, "한글 기관이름의 문법이 적절하지 않습니다.")
		, ERR_KOR_ORG_ADDR(1105, "한글 기관주소의 문법이 적절하지 않습니다.")
		, ERR_KOR_ORG_ADDR_DTL(1106, "한글 기관세부주소의 문법이 적절하지 않습니다.")
		, ERR_ENG_ORG_NAME(1107, "영문 기관이름의 문법이 적절하지 않습니다.")
		, ERR_ENG_ORG_ADDR(1108, "영문 기관주소의 문법이 적절하지 않습니다.")
		, ERR_ENG_ORG_ADDR_DTL(1109, "영문 기관세부주소의 문법이 적절하지 않습니다.")
		, ERR_ORG_ZIPCODE(1110, "기관 우편번호의 문법이 적절하지 않습니다.")
		, ERR_NETWORK_NAME(1111, "네트워크 이름의 문법이 적절하지 않습니다.")
		, ERR_NETWORK_TYPE(1112, "네트워크 분류의 문법이 적절하지 않습니다.")
		, ERR_SERVICE_TYPE(1113, "서비스 분류의 문법이 적절하지 않습니다.")
		, ERR_ORG_TYPE(1114, "기관 분류의 문법이 적절하지 않습니다.")
		, ERR_SERVICE_LOCATION(1115, "서비스 지역의 문법이 적절하지 않습니다.")
		, ERR_KOR_ADM_NAME(1116, "관리자 한글이름의 문법이 적절하지 않습니다.")
		, ERR_KOR_ADM_ORG_NAME(1117, "관리자 한글 기관이름의 문법이 적절하지 않습니다.")
		, ERR_KOR_ADM_ORG_ADDR(1118, "관리자 한글 기관주소의 문법이 적절하지 않습니다.")
		, ERR_KOR_ADM_ORG_ADDR_DTL(1119, "관리자 한글 기관세부주소의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_NAME(1120, "관리자 영문이름의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_ORG_NAME(1121, "관리자 영문 기관이름의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_ORG_ADDR(1122, "관리자 영문 기관주소의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_ORG_ADDR_DTL(1123, "관리자 영문 기관세부주소의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_ORG_ZIPCODE(1124, "관리자 기관 우편번호의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_ORG_TELNO(1125, "관리자 기관 전화번호의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_ORG_MAIN_TELNO(1126, "관리자 기관 대표번호의 문법이 적절하지 않습니다.")
		, ERR_ENG_ADM_EMAIL(1127, "관리자 이메일의 문법이 적절하지 않습니다.")
		, ERR_NOW_HOST(1128, "즉시 사용 호스트수는 숫자만을 허용합니다.")
		, ERR_NOW_USE(1129, "즉시 사용 내역은 숫자만을 허용합니다.")
		, ERR_6MONTH_HOST(1130, "6개월간 소요예상 호스트수는 숫자만을 허용합니다.")
		, ERR_6MONTH_USE(1131, "6개월간 소요예상 내역은 숫자만을 허용합니다.")
		, ERR_12MONTH_HOST(1132, "12개월간 소요예상 호스트수는 숫자만을 허용합니다.")
		, ERR_12MONTH_USE(1133, "12개월간 소요예상 내역은 숫자만을 허용합니다.")
		, ERR_COMMENT(1134, "추가사항의 문법이 적절하지 않습니다.")
		, ERR_PUBLISH(1135, "공개여부 문법이 적절하지 않습니다.")
		;
		
		private int code;
		private String msg;

		private EPPSyntaxErrorCodes(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public int getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	public enum EPPSemanticErrorCodes {
		ERR_NOT_AVAILABLE_PREFIX(1201, "IPv4주소는 1,2,4,8,16,32,64...16777216개로만 할당이 가능 합니다.")
		, ERR_NOT_ALLOC_ISP(1202, "해당 ISP에게 할당된(배정된) IPv4주소가 아닙니다.")
		, ERR_NOT_ALLOC_IP(1203, "할당된 IPv4주소가 아닙니다.")
		, ERR_NOT_ALLOC_ORG(1204, "해당 기관에 할당된 IPv4주소가 아닙니다.")
		, ERR_EXIST_IP_BLOCK_1(1205, "이미 할당된 주소블럭이 해당 주소블럭과 겹쳐서는 안됩니다.")
		, ERR_EXIST_IP_BLOCK_2(1206, "해당 주소블럭이 이미 할당된 주소블럭에 겹쳐서는 안됩니다.")
		, ERR_EXIST_NETWORK_NAME(1207, "이미 사용되고 있는 네트워크 이름입니다.")
		, ERR_NOT_EXIST_ORG_NETWORK_NAME(1208, "해당 네트워크 이름을 가지는 기관이 존재하지 않습니다.")
		, ERR_NOT_AVAILABLE_NETWORK_TYPE(1209, "지원되지 않는 네트워크 분류입니다.")
		, ERR_NOT_AVAILABLE_SERVICE_TYPE(1210, "지원되지 않는 서비스 분류입니다.")
		, ERR_NOT_AVAILABLE_ORG_TYPE(1211, "지원되지 않는 기관 분류입니다.")
		, ERR_NOT_AVAILABLE_SERVICE_LOCATION(1212, "지원되지 않는 서비스 지역입니다.")
		, ERR_NOT_AVAILABLE_NOW_HOST_PERCENT_25(1213, "즉시 사용 호스트수는 할당 호스트수의 25%이상 이어야 합니다.")
		, ERR_NOT_AVAILABLE_HOST_ALLOC_HOST(1214, "각 호스트수는 할당 호스트수 이하여야 합니다.")
		, ERR_NOT_CHANGE_IP_INFRA(1215, "해당 주소는 인프라용으로 할당된 주소입니다. 변경이 불가능합니다.")
		;
		private int code;
		private String msg;
		
		private EPPSemanticErrorCodes(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public int getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	public enum EPPSystemErrorCodes {
		ERR_CONNECT_REFUSE(2000, "접속단절")
		, ERR_SESSION_REFUSE(2001, "세션단절")
		, ERR_SOCKET_ERROR(2002, "소켓관련오류")
		, ERR_SOCKET_REFUSE(2003, "소켓단절")
		, ERR_INPUT_ERROR(2004, "입력관련오류")
		, ERR_INPUT_REFUSE(2005, "입력단절")
		, ERR_OUTPUT_ERROR(2006, "출력관련오류")
		, ERR_OUTPUT_REFUSE(2007, "출력단절")
		, ERR_SERVER_STANDBY(2008, "서버대기상태")
		, ERR_SQL_ERROR(2100, "SQL오류")
		, ERR_CONNECT_OPEN(2101, "접속오류")
		, ERR_CONNECT_CLOSE(2102, "접속해제오류")
		, ERR_NOT_WELLFORMED_XML(2103, "XML관련 오류(not well formed)")
		, ERR_INVALID_XML(2104, "XML유효성 오류(invalid)")
		, ERR_MESSAGE_HANDLING(2105, "메세지 핸들링 오류")
		, ERR_NOT_AVAILABLE(9995, "허용되지 않음")
		, ERR_ALREADY_PROGRESS(9996, "이미 수행됨")
		, ERR_UNKNOWN(9997, "알수 없음")
		, ERR_NOT_EXIST(9998, "존재하지 않음")
		, ERR_SYSTEM_ERROR(9999, "시스템 오류")
		;
		private int code;
		private String msg;
		
		private EPPSystemErrorCodes(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public int getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	public enum EPPErrorTypeCodes {
		ERR_SYNTAX_TYPE(1, "error_syntax")
		, ERR_SEMANTIC_TYPE(2, "error_sematic")
		, ERR_SYSTEM_TYPE(3, "error_system")
		, NONE(4, "");
		private int code;
		private String path;
		private EPPErrorTypeCodes(int code, String path) {
			this.code = code;
			this.path = path;
		}
		public int getCode() {
			return code;
		}
		public String getPath() {
			return path;
		}
	}
	
	public static EPPErrorTypeCodes findEPPErrorTypeCode(int code) {
		EPPSyntaxErrorCodes[] eppSyntaxErrorCodes = EPPSyntaxErrorCodes.values();
		for (EPPSyntaxErrorCodes errCode : eppSyntaxErrorCodes) {
			if (errCode.getCode() == code) {
				return EPPErrorTypeCodes.ERR_SYNTAX_TYPE;
			}
		}
		EPPSemanticErrorCodes[] eppSemanticErrorCodes = EPPSemanticErrorCodes.values();
		for (EPPSemanticErrorCodes errCode : eppSemanticErrorCodes) {
			if (errCode.getCode() == code) {
				return EPPErrorTypeCodes.ERR_SEMANTIC_TYPE;
			}
		}
		EPPSystemErrorCodes[] eppSystemErrorCodes = EPPSystemErrorCodes.values();
		for (EPPSystemErrorCodes errCode : eppSystemErrorCodes) {
			if (errCode.getCode() == code) {
				return EPPErrorTypeCodes.ERR_SYSTEM_TYPE;
			}
		}
		
		return EPPErrorTypeCodes.NONE;
	}
	
	public static String findEPPErrorTypeMsg(int code) {
		EPPSyntaxErrorCodes[] eppSyntaxErrorCodes = EPPSyntaxErrorCodes.values();
		for (EPPSyntaxErrorCodes errCode : eppSyntaxErrorCodes) {
			if (errCode.getCode() == code) {
				return errCode.getMsg();
			}
		}
		EPPSemanticErrorCodes[] eppSemanticErrorCodes = EPPSemanticErrorCodes.values();
		for (EPPSemanticErrorCodes errCode : eppSemanticErrorCodes) {
			if (errCode.getCode() == code) {
				return errCode.getMsg();
			}
		}
		EPPSystemErrorCodes[] eppSystemErrorCodes = EPPSystemErrorCodes.values();
		for (EPPSystemErrorCodes errCode : eppSystemErrorCodes) {
			if (errCode.getCode() == code) {
				return errCode.getMsg();
			}
		}
		
		return "성공적으로 처리되었습니다.";
	}

}


