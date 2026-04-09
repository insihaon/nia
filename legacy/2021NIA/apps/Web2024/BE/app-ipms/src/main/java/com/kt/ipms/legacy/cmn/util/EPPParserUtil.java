package com.kt.ipms.legacy.cmn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.irms.epp.xml.EPPResponseAction;
import org.irms.epp.xml.EPPXMLParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;

@Component
public class EPPParserUtil {
	
	private final int INDEX_SUM = 39;
	
	public EPPResponseAction parserResultFile(File file) {
		EPPResponseAction eppResponseAction = null;
		List<Integer> idxList = new ArrayList<Integer>();
		List<String> xmlList = new ArrayList<String>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			StringBuilder stringBuilder = new StringBuilder();
			byte[] buf = new byte[1024];
			while (fis.read(buf) != -1) {
				stringBuilder.append(new String(buf).trim());
			}
			
			String temp = stringBuilder.toString();
			int idx = 0;
			while (temp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", idx) != -1) {
				int index = temp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", idx);
				idxList.add(index);
				idx = index + INDEX_SUM;
			}
			
			for (int i=0; i < idxList.size(); i++) {
				if (i == idxList.size() - 1) {
					xmlList.add(temp.substring(idxList.get(i)));
				} else {
					xmlList.add(temp.substring(idxList.get(i), idxList.get(i+1)));
				}
			}
			for (String xml : xmlList) {
				EPPXMLParser xmlParser = new EPPXMLParser();
				try {
					Document doc = xmlParser.createDocument(xml, true, true);
					eppResponseAction = new EPPResponseAction();
					eppResponseAction.mapping(doc);
					break;
				} catch (Exception e) {
					continue;
				}
			}
			
		} catch (FileNotFoundException e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"WHOIS 결과 파일 업데이트"});
		} catch (IOException e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"WHOIS 결과 파일 업데이트"});
		} finally {
			if (fis != null) {
				try { fis.close(); } catch (IOException e) { }
			}
		}
		return eppResponseAction;
	}
	
//	public List<EPPResponseAction> parserResultFiles(File[] files) {
//		List<EPPResponseAction> eppResponseActions = new ArrayList<EPPResponseAction>();
//		List<Integer> idxList = new ArrayList<Integer>();
//		List<String> xmlList = new ArrayList<String>();
//		StringBuilder stringBuilder = new StringBuilder();
//		for (File file : files) {
//			file = file.getAbsoluteFile();
//			file.setReadable(true);
//			FileInputStream fis = null;
//			Scanner scanner = null;
//			try {
//				if (file.exists()) {
//					fis = new FileInputStream(file);
//					scanner = new Scanner(fis, "UTF-8");
//					PrintLogUtil.printLog("FOUND : " + file.getPath());
//					while (scanner.hasNext()) {
//						String line = scanner.nextLine();
//						stringBuilder.append(line);
//					}
//				} else {
//					PrintLogUtil.printLog("NOT FOUND : " + file.getPath());
//				}
//			} catch (Exception e) {
//				PrintLogUtil.printError(e);
//				continue;
//			} finally {
//				if (fis != null) {
//					try { fis.close(); } catch (IOException e) { }
//				}
//				if (scanner != null) {
//					scanner.close();
//				}
//			}
//		}
//		if (StringUtils.hasText(stringBuilder.toString())) {
//			String temp = stringBuilder.toString();
//			int idx = 0;
//			while (temp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", idx) != -1) {
//				int index = temp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", idx);
//				idxList.add(index);
//				idx = index + INDEX_SUM;
//			}
//			
//			for (int i=0; i < idxList.size(); i++) {
//				if (i == idxList.size() - 1) {
//					xmlList.add(temp.substring(idxList.get(i)));
//				} else {
//					xmlList.add(temp.substring(idxList.get(i), idxList.get(i+1)));
//				}
//			}
//			for (String xml : xmlList) {
//				EPPXMLParser xmlParser = new EPPXMLParser();
//				PrintLogUtil.printLog(xml);
//				try {
//					Document doc = xmlParser.createDocument(xml, true, true);
//					if (isValidDocument(xml)) {
//						EPPResponseAction eppResponseAction = new EPPResponseAction();
//						eppResponseAction.mapping(doc);
//						eppResponseActions.add(eppResponseAction);
//					}
//				} catch (Exception e) {
//					PrintLogUtil.printLog(e.getMessage());
//					continue;
//				}
//			}
//		}
//		return eppResponseActions;
//	}
	
	
	public List<EPPResponseAction> parserResultFiles(File[] files) {
		List<EPPResponseAction> eppResponseActions = new ArrayList<EPPResponseAction>();
		List<Integer> idxList = new ArrayList<Integer>();
		List<String> xmlList = new ArrayList<String>();
		StringBuilder stringBuilder = new StringBuilder();
		byte[] buf = new byte[1024 * 10];
		for (File file : files) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				for (int i=0; i < buf.length; i++) {
					buf[i] = 0;
				}
				while (fis.read(buf) != -1) {
					String readStr = new String(buf).trim();
					stringBuilder.append(readStr);
				}
			} catch (Exception e) {
				continue;
			} finally {
				if (fis != null) {
					try { fis.close(); } catch (IOException e) { }
				}
			}
		}
		if (StringUtils.hasText(stringBuilder.toString())) {
			String temp = stringBuilder.toString();
			int idx = 0;
			while (temp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", idx) != -1) {
				int index = temp.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", idx);
				idxList.add(index);
				idx = index + INDEX_SUM;
			}
			
			for (int i=0; i < idxList.size(); i++) {
				if (i == idxList.size() - 1) {
					xmlList.add(temp.substring(idxList.get(i)));
				} else {
					xmlList.add(temp.substring(idxList.get(i), idxList.get(i+1)));
				}
			}
			for (String xml : xmlList) {
				EPPXMLParser xmlParser = new EPPXMLParser();
				try {
					Document doc = xmlParser.createDocument(xml, true, true);
					if (isValidDocument(xml)) {
						EPPResponseAction eppResponseAction = new EPPResponseAction();
						eppResponseAction.mapping(doc);
						eppResponseActions.add(eppResponseAction);
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		return eppResponseActions;
	}
	
	private boolean isValidDocument(String xml) {
		boolean isValid = false;
		InputSource is = new InputSource(new StringReader(xml));
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "//resData";

			Node node = (Node) xpath.compile(expression).evaluate(doc, XPathConstants.NODE);
			if (node != null) {
//				expression = "//clTRID";
//				node = (Node) xpath.compile(expression).evaluate(doc, XPathConstants.NODE);
//				if (node != null) {
//					System.out.println(node.getTextContent());
//				}
				return true;
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}

}
