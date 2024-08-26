package com.kt.ipms.legacy.cmn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;

@Component
public class TraceParserUtil {
	
	public boolean checkEmptyTrace(String data) {
		boolean isNotEmpty = true;
		try {
			if(data.trim().indexOf("traceroute") > -1 ) {
				isNotEmpty = false;
			}
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
		}
		return isNotEmpty;
	}
	
	public Integer matchSeqTrace(String data) {
		Integer result = null;
		try {
			Pattern pattern = Pattern.compile(CommonCodeUtil.TRACE_SEQ_PATTERN, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(data);	
			if (matcher.find()) {
				result = Integer.valueOf(matcher.group());
			} else {
				throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
		}
		return result;
	}
	
	public String matchIPV4Address(String data) {
		String result = "";
		try {
			Pattern pattern = Pattern.compile(CommonCodeUtil.TRACE_IPV4ADDR_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher matcher = pattern.matcher(data);	
			if (matcher.find()) {
				result = matcher.group();
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00023", new String[]{"Trace Route"});
		}
		return result;
	}
	
	public String matchIPV6Address(String data) {
		String result = "";
		try {
			String[] subStr = data.trim().split(" ");
			if (subStr.length > 2) {
				result = subStr[2];
			}			
			if(result.equalsIgnoreCase("*"))
				result ="";
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00023", new String[]{"Trace Route"});
		}
		return result;
	}
	
	public String matchASOrgCd(String data) {
		String result = "";
		try {
			Pattern pattern = Pattern.compile(CommonCodeUtil.TRACE_ASORGCD_PATTERN1 + CommonCodeUtil.TRACE_ASORGCD_PATTERN2,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(data);
			if (matcher.find()) {
				result = matcher.group(1);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00023", new String[]{"Trace Route"});
		}
		return result;
	}

}
