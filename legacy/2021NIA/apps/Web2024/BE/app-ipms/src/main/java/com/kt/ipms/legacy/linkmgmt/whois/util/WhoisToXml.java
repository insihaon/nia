package com.kt.ipms.legacy.linkmgmt.whois.util;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WhoisToXml  implements XmlObj {
	
	private String reqType;				// WHOIS 신청서 종류 (NEW/ADD/MOD/RTN)
	
	private String clTRID;					
	
	private String startIp;					// 시작IP
	private String endIp;					// 끝IP
	
	private String koOrgName;			// 한글 기관명
	private String koOrgAddr;				// 한글 주소
	private String koOrgAddrDtl;			// 한글 상세 주소
	private String enOrgName;			// 영문 기관명
	private String enOrgAddr;				// 영문 주소
	private String enOrgAddrDtl;			// 영문 상세 주소
	private String orgPhone;				// 기관 전화번호	
	private String orgEmail;				// 기관 이메일
	private String orgPost;					// 기관 우편번호
	
	private String netName;				// 네트워크 이름
	private String netType;				// 네트워크  분류
	private String svcType;				// 서비스 분류
	private String ipType;					// IP 분류
	private String orgType;				// 이용기관 분류
	private String svcLoc;					// 서비스 지역
	
	private String comment;				// 추가 사항
	
	private String rtnReason;			// 반납사유
	
	public String toXml() {
		
		String XmlString = "";
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("ROOT");
			Element childElement = doc.createElement("CONTENT");
			doc.appendChild(rootElement);
			rootElement.appendChild(childElement);
			
			Class<? extends Object> cls = WhoisToXml.class;
			
			java.lang.reflect.Method methodList [] = cls.getDeclaredMethods();
			for(int i=0; i<methodList.length; i++) {
				java.lang.reflect.Method m = methodList[i];
				
				if(m.isAnnotationPresent(XmlTag.class)) {
				
					XmlTag tag = m.getAnnotation(XmlTag.class);
					String nodeName = tag.XmlName();
					String val = null;
					
					if(m.invoke(this) != null) {
						val = m.invoke(this).toString();
						
						
						Element childNode = doc.createElement(nodeName);
						childNode.appendChild(doc.createTextNode(val));
						childElement.appendChild(childNode);
					}
					
					// if(val == null) val = "";
					
				}
			}
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transFomer = transFactory.newTransformer();
			transFomer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DOMSource domSource = new DOMSource(rootElement);
			
			StringWriter w = new StringWriter();
			StreamResult r = new StreamResult(w);
			
			transFomer.transform(domSource, r);
			XmlString = w.getBuffer().toString();
			
		} catch (ParserConfigurationException e) {
			// TODO: handle exception
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return XmlString;
	}


	@XmlTag (XmlName = "reqType")
	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	@XmlTag (XmlName = "startIp")
	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	@XmlTag (XmlName = "endIp")
	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	@XmlTag (XmlName = "koOrgName")
	public String getKoOrgName() {
		return koOrgName;
	}

	public void setKoOrgName(String koOrgName) {
		this.koOrgName = koOrgName;
	}

	@XmlTag (XmlName = "koOrgAddr")
	public String getKoOrgAddr() {
		return koOrgAddr;
	}

	public void setKoOrgAddr(String koOrgAddr) {
		this.koOrgAddr = koOrgAddr;
	}

	@XmlTag (XmlName = "enOrgName")
	public String getEnOrgName() {
		return enOrgName;
	}

	public void setEnOrgName(String enOrgName) {
		this.enOrgName = enOrgName;
	}

	@XmlTag (XmlName = "enOrgAddr")
	public String getEnOrgAddr() {
		return enOrgAddr;
	}

	public void setEnOrgAddr(String enOrgAddr) {
		this.enOrgAddr = enOrgAddr;
	}

	@XmlTag (XmlName = "orgPhone")
	public String getOrgPhone() {
		return orgPhone;
	}

	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}

	@XmlTag (XmlName = "orgEmail")
	public String getOrgEmail() {
		return orgEmail;
	}

	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}

	@XmlTag (XmlName = "orgPost")
	public String getOrgPost() {
		return orgPost;
	}

	public void setOrgPost(String orgPost) {
		this.orgPost = orgPost;
	}

	@XmlTag (XmlName = "netName")
	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	@XmlTag (XmlName = "netType")
	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	@XmlTag (XmlName = "svcType")
	public String getSvcType() {
		return svcType;
	}

	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}

	@XmlTag (XmlName = "ipType")
	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}

	@XmlTag (XmlName = "orgType")
	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	@XmlTag (XmlName = "svcLoc")
	public String getSvcLoc() {
		return svcLoc;
	}

	public void setSvcLoc(String svcLoc) {
		this.svcLoc = svcLoc;
	}

	@XmlTag (XmlName = "comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@XmlTag (XmlName = "koOrgAddrDtl")
	public String getKoOrgAddrDtl() {
		return koOrgAddrDtl;
	}

	public void setKoOrgAddrDtl(String koOrgAddrDtl) {
		this.koOrgAddrDtl = koOrgAddrDtl;
	}

	@XmlTag (XmlName = "enOrgAddrDtl")
	public String getEnOrgAddrDtl() {
		return enOrgAddrDtl;
	}

	public void setEnOrgAddrDtl(String enOrgAddrDtl) {
		this.enOrgAddrDtl = enOrgAddrDtl;
	}

	@XmlTag (XmlName = "clTRID")
	public String getClTRID() {
		return clTRID;
	}


	public void setClTRID(String clTRID) {
		this.clTRID = clTRID;
	}


	@XmlTag (XmlName = "rtnReason")
	public String getRtnReason() {
		return rtnReason;
	}


	public void setRtnReason(String rtnReason) {
		this.rtnReason = rtnReason;
	}

	public void fromXml(String xml) {
		// TODO Auto-generated method stub
		
	}
	
	

}
