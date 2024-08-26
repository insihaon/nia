package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbScrnAuthVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 1637985600670745959L;

	private String suserGradeCd;

	private String sscrnId;

	private String sscrnAutUseYn;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSscrnId(String sscrnId) {
		this.sscrnId = sscrnId;
	}

	public String getSscrnId() {
		return sscrnId;
	}

	public void setSscrnAutUseYn(String sscrnAutUseYn) {
		this.sscrnAutUseYn = sscrnAutUseYn;
	}

	public String getSscrnAutUseYn() {
		return sscrnAutUseYn;
	}

	/** MEMBER METHOD DECLARATION END **/
}