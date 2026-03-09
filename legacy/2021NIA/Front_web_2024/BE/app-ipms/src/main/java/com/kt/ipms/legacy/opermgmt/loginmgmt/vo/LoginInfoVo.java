package com.kt.ipms.legacy.opermgmt.loginmgmt.vo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
import com.codej.base.dto.BaseUser;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;

public class LoginInfoVo  extends TbUserBasVo  implements Serializable {
	
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8641525113975177595L;
	
	
	// private String sUserId;   //  사용자 PW
	private String sUserPw;   //  사용자 PW
	private String sConnIP;	  //  접속 IP 
	private Date dloginDt;   // 로그인 시간
	private Date dlogoutDt;  //  로그아웃 시간
	private Date dconnConfDt;   //  접속 확인 일시
	private String sConnRsltCD;   //  접속 결과 
	private String sConnMsg;    // 접속 오류 메세지
	private Integer nhndsetApySeq;  // 단말 SEQ
	private String sapplSrvrId;     // AP 명
	private TbMenuBasListVo menuBasListVo;
	
	private TbLvlBasListVo userAuthListVo ;
	/** MEMBER VARIABLE DECLARATION END **/	

	// public String getsUserId() {
	// 	return sUserId;
	// }
	// public void setsUserId(String sUserId) {
	// 	this.sUserId = sUserId;
	// }
	public String getsUserPw() {
		return sUserPw;
	}
	public void setsUserPw(String sUserPw) {
		this.sUserPw = sUserPw;
	}
	public String getsConnIP() {
		return sConnIP;
	}
	public void setsConnIP(String sConnIP) {
		this.sConnIP = sConnIP;
	}
	public Date getDloginDt() {
		return dloginDt;
	}
	public void setDloginDt(Date dloginDt) {
		this.dloginDt = dloginDt;
	}
	public Date getDlogoutDt() {
		return dlogoutDt;
	}
	public void setDlogoutDt(Date dlogoutDt) {
		this.dlogoutDt = dlogoutDt;
	}
	public Date getDconnConfDt() {
		return dconnConfDt;
	}
	public void setDconnConfDt(Date dconnConfDt) {
		this.dconnConfDt = dconnConfDt;
	}
	public String getsConnRsltCD() {
		return sConnRsltCD;
	}
	public void setsConnRsltCD(String sConnRsltCD) {
		this.sConnRsltCD = sConnRsltCD;
	}
	public String getsConnMsg() {
		return sConnMsg;
	}
	public void setsConnMsg(String sConnMsg) {
		this.sConnMsg = sConnMsg;
	}
		
	public Integer getNhndsetApySeq() {
		return nhndsetApySeq;
	}
	public void setNhndsetApySeq(Integer nhndsetApySeq) {
		this.nhndsetApySeq = nhndsetApySeq;
	}	
	
	public String getSapplSrvrId() {
		return sapplSrvrId;
	}
	public void setSapplSrvrId(String sapplSrvrId) {
		this.sapplSrvrId = sapplSrvrId;
	}
	public TbMenuBasListVo getMenuBasListVo() {
		return menuBasListVo;
	}
	
	public void setMenuBasListVo(TbMenuBasListVo menuBasListVo) {
		this.menuBasListVo = menuBasListVo;
	}
	public TbLvlBasListVo getUserAuthListVo() {
		return userAuthListVo;
	}
	public void setUserAuthListVo(TbLvlBasListVo userAuthListVo) {
		this.userAuthListVo = userAuthListVo;
	}
	
	
	
}
