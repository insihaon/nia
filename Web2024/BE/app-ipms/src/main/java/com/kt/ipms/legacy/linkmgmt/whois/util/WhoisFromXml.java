package com.kt.ipms.legacy.linkmgmt.whois.util;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.omg.CORBA.NVList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;

/**
 * Whois Response Xml Pasring
 */
public class WhoisFromXml {

	private WhoisInfoObj obj = new WhoisInfoObj();
	
	public WhoisInfoObj fromXml(String xml) {

		String str = null;
		
		try {
			
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			
			doc.getDocumentElement().normalize();
			
			Node root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			
			Node node = null;
			String nodeName = null;
			
			for(int i=0; i<nodes.getLength(); i++) {
				node = nodes.item(i);
				nodeName = node.getNodeName();
				
				NodeList resNodes = node.getChildNodes();
				Node resNode = null;
				String resNodeName = null;
				Node resultsNode = null;
				NodeList resultsNodes = null;
				
				
				for(int j=0; j<resNodes.getLength(); j++) {
					resNode = resNodes.item(j);
					resNodeName = resNode.getNodeName();
				
					String tagName = null;
					String tagValue = null;
					
					Element el = null;
					Node n = null;
					
					NodeList results = null;
					
					if(resNodeName.equals("result")) {
						
						results = resNode.getChildNodes();
						
						for(int k=0; k<results.getLength(); k++) {
							el = (Element) results.item(k);
							n = el.getFirstChild();
							tagName = results.item(k).getNodeName();
							tagValue = n.getNodeValue();
						
						}
					} else if(resNodeName.equals("resData")) {
						
						results = resNode.getChildNodes();
						
						for(int o=0; o<results.getLength(); o++) {
							resultsNode = results.item(o);
							resultsNodes = resultsNode.getChildNodes();		// ipv4:infData
							
							setResponseValues(resultsNodes);
							
						}
					} else if(resNodeName.equals("trID")) {
						
						results = resNode.getChildNodes();
						
						setTransValues(results);
					
					}
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	/**
	 * Set Values
	 * @param nodeList
	 */
	public void setResponseValues(NodeList nodeList) {
		
		try {
			
			Class<? extends Object> cls = obj.getClass();
			java.lang.reflect.Field fields [] = cls.getDeclaredFields();
			java.lang.reflect.Field f = null;
			
			for(int i=0; i<fields.length; i++) {
				
				Node node = null;
				String tagName = null;
				String tagValue = null;
				
				Element el = null;
				Node n = null;
				
				f = fields[i];

				for(int j=0; j<nodeList.getLength(); j++) {
					node = nodeList.item(j);
					el = (Element) nodeList.item(j);
					n = el.getFirstChild(); 
					tagName = node.getNodeName();
					tagValue = n.getNodeValue();
					
					if(null == tagValue) tagValue = "";
					
					if( null != node) {
						
						if(tagName != null) {
							
							tagName = tagName.split(":")[1];
							
							if(tagName.equals("org")) {
								
								if(node.getChildNodes().getLength() > 0) {
									
									NodeList childNode = node.getChildNodes();
									setOrgValues(childNode);
								}
							} else if(tagName.equals("network")) {
								if(node.getChildNodes().getLength() > 0) {
									
									NodeList childNode = node.getChildNodes();
									setNetValues(childNode);
								}
							} else {
								PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
							}
							
							
							if(null != tagValue &&  f.getName().toString().equals(tagName))  {
								cls.getField(tagName).set(obj, tagValue);								
							} 
							
						}
					}
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setTransValues(NodeList nodeList) {

		try {
			
			Class<? extends Object> cls = obj.getClass();
			java.lang.reflect.Field fields [] = cls.getDeclaredFields();
			java.lang.reflect.Field f = null;
			
			for(int i=0; i<fields.length; i++) {
				
				Node node = null;
				String tagName = null;
				String tagValue = null;
				
				Element el = null;
				Node n = null;
				
				f = fields[i];
				
				String[] tmpSplit = null;
				
				for(int j=0; j<nodeList.getLength(); j++) {
					node = nodeList.item(j);
					el = (Element) nodeList.item(j);
					n = el.getFirstChild(); 
					tagName = node.getNodeName();
					tagValue = n.getNodeValue();
					
					if(null != tagValue && tagName.equals("clTRID")) {
						tmpSplit = tagValue.split("-");
						tagValue = tmpSplit[0] + "-" +  tagValue.split("-")[1] + "-" +  tagValue.split("-")[2];
					}
					
					if(null == tagValue) tagValue = "";
					
					if( null != node) {
						
						if(tagName != null) {
							
							if(null != tagValue &&  f.getName().toString().equals(tagName))  {
								
								cls.getField(tagName).set(obj, tagValue);								
							}
							
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setOrgValues(NodeList nodeList) {

		try {
			
			Class<? extends Object> cls = obj.getClass();
			java.lang.reflect.Field fields [] = cls.getDeclaredFields();
			java.lang.reflect.Field f = null;
			
			for(int i=0; i<fields.length; i++) {
				
				Node node = null;
				String tagName = null;
				String tagValue = null;
				
				Element el = null;
				Node n = null;
				
				f = fields[i];
				
				for(int j=0; j<nodeList.getLength(); j++) {
					node = nodeList.item(j);
					el = (Element) nodeList.item(j);
					n = el.getFirstChild(); 
					tagName = node.getNodeName();
					tagValue = n.getNodeValue();
					
					if(null == tagValue) tagValue = "";
					
					NodeList childNodes = null;
					Node childNode = null;
					Element childEl = null;
					Node childN = null;
					
					if(null != tagName) {
						tagName = tagName.split(":")[1];	
					}
					
					if(tagName.equals("kor")) {
						childNodes = node.getChildNodes();
						
						for(int k=0; k<childNodes.getLength(); k++) {
							childNode = childNodes.item(k);
							childEl = (Element) childNodes.item(k);
							childN = childEl.getFirstChild();
							tagName = childNode.getNodeName();
							tagValue = childN.getNodeValue();
							
							if(null != tagName) {
								tagName = tagName.split(":")[1];	
							}
							
							if(tagName.equals("name")) {
								obj.setKoOrgName(tagValue);
							} else if(tagName.equals("addr")) {
								obj.setKoOrgAddr(tagValue);
							}
							
						}
					} else if(tagName.equals("eng")) {
						childNodes = node.getChildNodes();
						
						for(int k=0; k<childNodes.getLength(); k++) {
							childNode = childNodes.item(k);
							childEl = (Element) childNodes.item(k);
							childN = childEl.getFirstChild();
							tagName = childNode.getNodeName();
							tagValue = childN.getNodeValue();
							
							if(null != tagName) {
								tagName = tagName.split(":")[1];	
							}
							
							if(tagName.equals("name")) {
								obj.setEnOrgName(tagValue);
							} else if(tagName.equals("addr")) {
								obj.setEnOrgAddr(tagValue);
							}
							
						}
					} else {
						if(null != node) {
							
							if(tagName != null) {
								if(tagName.equals("phone")) {
									obj.setOrgPhone(tagValue);
								} else if(tagName.equals("post")) {
									obj.setOrgPost(tagValue);
								} else if(tagName.equals("e_mail")) {
									obj.setOrgEmail(tagValue);
								}
							}
						}
						
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setNetValues(NodeList nodeList) {

		try {
			
			Class<? extends Object> cls = obj.getClass();
			java.lang.reflect.Field fields [] = cls.getDeclaredFields();
			java.lang.reflect.Field f = null;
			
			for(int i=0; i<fields.length; i++) {
				
				Node node = null;
				String tagName = null;
				String tagValue = null;
				
				Element el = null;
				Node n = null;
				
				f = fields[i];
				
				for(int j=0; j<nodeList.getLength(); j++) {
					node = nodeList.item(j);
					el = (Element) nodeList.item(j);
					n = el.getFirstChild(); 
					tagName = node.getNodeName();
					tagValue = n.getNodeValue();
					
					if(null == tagValue) tagValue = "";
					
					if(null != tagName) {
						tagName = tagName.split(":")[1];	
					}
					
					if(tagName.equals("name")) {
						obj.setNetName(tagValue);
					} else if(tagName.equals("netType")) {
						obj.setNetType(tagValue);
					} else if(tagName.equals("svcType")) {
						obj.setSvcType(tagValue);
					} else if(tagName.equals("orgType")) {
						obj.setOrgType(tagValue);
					} else if(tagName.equals("svcLoc")) {
						obj.setSvcLoc(tagValue);
					} else if(tagName.equals("ipType")) {
						obj.setIpType(tagValue);
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
