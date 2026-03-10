package com.kt.ipms.legacy.cmn.util;
public class TACSCodeUtil {
	/** TACS 에러 코드 정의 **/
	public enum TACSErrorCodes {
		OG_ERR_NO(0, "에러 없음")
		, OG_ERR_NOT_FIND_DEVICE(1, "등록된 장비가 없음")
		, OG_ERR_LOGIN_FAIL(2, "로그인 실패")
		, OG_ERR_REQUEST_FAIL(3, "요청 실패 (릴레이가 없거나 망문제)")
		, OG_ERR_NOT_FIND_SERVERID(4, "요청한 서버 ID가 존재 하지 않음")
		, OG_ERR_NOT_PERMIT_MAC(10, "허가되지 않은 MAC 주소")
		, OG_ERR_NOT_PERMIT_IP(11, "허가되지 않은 IP 주소")
		, OG_ERR_NOT_PERMIT_DEVICE(12, "장비 권한 없음")
		, OG_ERR_CREATE_SOCKET_FAIL(61440, "소켓 생성 실패")
		, OG_ERR_OG_CONNECT_FAIL(61441, "OpenGateWay 서버 접속 실패")
		, OG_ERR_GW_CONNECT_FAIL(61442, "STG 서버 접속 실패")
		, OG_ERR_EWOULDBLOCK(61443, "우드 블록")
		, OG_ERR_PROTOCOL_REFUSE(61444, "알수 없는 에러")
		, OG_ERR_UNKNOWN(99999, "알수 없는 에러")
		;
		private int code;
		private String msg;
		private TACSErrorCodes(int code, String msg) {
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
	
	public static TACSErrorCodes findTACSErrorCode(int code) {
		TACSErrorCodes[] tacsErrorCodes = TACSErrorCodes.values();
		for (TACSErrorCodes errorCode : tacsErrorCodes) {
			if (errorCode.getCode() == code) {
				return errorCode;
			}
		}
		return TACSErrorCodes.OG_ERR_UNKNOWN;
	}
	
	/** TACS NEGOCIATION CODE **/
	public static final byte IAC = (byte) 255;
	public static final byte DONT = (byte) 254;
	public static final byte DO = (byte) 253;
	public static final byte WONT = (byte) 252;
	public static final byte WILL = (byte) 251;
}