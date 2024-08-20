package com.kt.ipms.legacy.cmn.util;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

@Component
public class IpCalculateUtil {
	
	
	public BigInteger getStartIpAddressDecimal(String sipBlock, int nbitmask, String sipVersionTypeCd) {
		BigInteger baseVal = getStringToDecimal(sipBlock, sipVersionTypeCd);
		BigInteger resultVal = null;
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			BigInteger mask = new BigInteger(CommonCodeUtil.IPV4_MASK, 16);
			mask = mask.shiftRight(nbitmask);
			mask = mask.not();
			resultVal = mask.and(baseVal);
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			BigInteger mask = new BigInteger(CommonCodeUtil.IPV6_MASK, 16);
			mask = mask.shiftRight(nbitmask);
			mask = mask.not();
			resultVal = mask.and(baseVal);
		}
		return resultVal;
	}
	
	public BigInteger getEndIpAddressDecimal(String sipBlock, int nbitmask, String sipVersionTypeCd) {
		BigInteger baseVal = getStringToDecimal(sipBlock, sipVersionTypeCd);
		BigInteger resultVal = null;
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			BigInteger mask = new BigInteger(CommonCodeUtil.IPV4_MASK, 16);
			mask = mask.shiftRight(nbitmask);
			resultVal = mask.or(baseVal);
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			BigInteger mask = new BigInteger(CommonCodeUtil.IPV6_MASK, 16);
			mask = mask.shiftRight(nbitmask);
			resultVal = mask.or(baseVal);
		}
		return resultVal;
	}
	
	public BigInteger getNetMaskDecimal(int nbitmask, String sipVersionTypeCd) {
		BigInteger netMaskInt = BigInteger.ZERO;
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			for (int i = CommonCodeUtil.IPV4_MAX_PREFIX - 1 ; i >= (CommonCodeUtil.IPV4_MAX_PREFIX - nbitmask); i--) {
				netMaskInt = netMaskInt.add(calculateSqrd(i));
			}
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			for (int i = CommonCodeUtil.IPV6_MAX_PREFIX - 1; i >= (CommonCodeUtil.IPV6_MAX_PREFIX - nbitmask); i--) {
				netMaskInt = netMaskInt.add(calculateSqrd(i));
			}
		}
		return netMaskInt;
	}
	
	public BigInteger getStringToDecimal(String sipBlock, String sipVersionTypeCd) {
		BigInteger resultIpAddress = null;
		String fullAddr = getFullIpAddress(sipBlock, sipVersionTypeCd);
		StringBuilder hexStr = new StringBuilder();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			String[] octets = fullAddr.split("\\.");
			for(int i = 0; i < octets.length; i++) {
				hexStr.append(String.format("%02x", Integer.parseInt(octets[i], 10)));
			}
			resultIpAddress = new BigInteger(hexStr.toString(), 16);
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			String[] octets = fullAddr.split(":");
			for (int i = 0; i < octets.length; i++) {
				hexStr.append(String.format("%04x", Integer.parseInt(octets[i], 16)));
			}
			resultIpAddress = new BigInteger(hexStr.toString(), 16);
		}
		return resultIpAddress;
	}
	
	public BigInteger calculateSqrd(int nbitmask) {
		BigInteger output = new BigInteger("2",10);
		output = output.pow(nbitmask);
		return output;
	}
	
	public String getDecimalToStringFull(BigInteger nipAddr, String sipVersionTypeCd) {
		String resultAddress = "";
		StringBuilder address = new StringBuilder();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			for (int i = 0; i < CommonCodeUtil.IPV4_OCTET_CNT; i++) {
				address.append(Integer.valueOf(String.format("%08x", nipAddr).substring(i*2, (i*2) + 2), 16));
				if (i < CommonCodeUtil.IPV4_OCTET_CNT - 1) {
					address.append(".");
				}
			}
			resultAddress = getFullIpAddress(address.toString(), sipVersionTypeCd);
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			String temp = String.format("%032x", nipAddr);
			for (int i = 0; i < CommonCodeUtil.IPV6_OCTET_CNT; i++) {
				address.append(temp.substring(i*4, (i*4) + 4));
				if (i < CommonCodeUtil.IPV6_OCTET_CNT - 1) {
					address.append(":");
				}
			}
			resultAddress = getFullIpAddress(address.toString(), sipVersionTypeCd);
		}
		return resultAddress;
	}
	
	public String getDecimalToStringShort(BigInteger nipAddr, String sipVersionTypeCd) {
		String resultAddress = "";
		StringBuilder address = new StringBuilder();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			for (int i = 0; i < CommonCodeUtil.IPV4_OCTET_CNT; i++) {
				address.append(Integer.valueOf(String.format("%08x", nipAddr).substring(i*2, (i*2) + 2), 16));
				if (i < CommonCodeUtil.IPV4_OCTET_CNT - 1) {
					address.append(".");
				}
			}
			resultAddress = getShortIpAddress(address.toString(), sipVersionTypeCd);
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			String temp = String.format("%032x", nipAddr);
			for (int i = 0; i < CommonCodeUtil.IPV6_OCTET_CNT; i++) {
				address.append(temp.substring(i*4, (i*4) + 4));
				if (i < CommonCodeUtil.IPV6_OCTET_CNT - 1) {
					address.append(":");
				}
			}
			resultAddress = getShortIpAddress(address.toString(), sipVersionTypeCd);
		}
		return resultAddress;
	}
	
	public String getDecimalToStringBinary(BigInteger nipAddr, String sipVersionTypeCd) {
		String resultAddress = "";
		StringBuilder address = new StringBuilder();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			for (int i = 0; i < CommonCodeUtil.IPV4_OCTET_CNT; i++) {
				address.append(Integer.valueOf(String.format("%08x", nipAddr).substring(i*2, (i*2) + 2), 16));
				if (i < CommonCodeUtil.IPV4_OCTET_CNT - 1) {
					address.append(".");
				}
			}
			resultAddress = getBinaryIpAddress(address.toString(), sipVersionTypeCd);
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			String temp = String.format("%032x", nipAddr);
			for (int i = 0; i < CommonCodeUtil.IPV6_OCTET_CNT; i++) {
				address.append(temp.substring(i*4, (i*4) + 4));
				if (i < CommonCodeUtil.IPV6_OCTET_CNT - 1) {
					address.append(":");
				}
			}
			resultAddress = getBinaryIpAddress(address.toString(), sipVersionTypeCd);
		}
		return resultAddress;
	}
	
	public String getFullIpAddress(String sipBlock, String sipVersionTypeCd) {
		StringBuilder address = new StringBuilder();
		String tempSipBlock = sipBlock;
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			String[] octets = tempSipBlock.split("\\.");
			for (int i = 0; i < octets.length; i++) {
				address.append(String.format("%03d", Integer.parseInt(octets[i], 10)));
				if (i < octets.length - 1) {
					address.append(".");
				}
			}
			
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			tempSipBlock = tempSipBlock.replace("::", ":*:");
			String[] octets = tempSipBlock.split(":");
			if (octets.length < CommonCodeUtil.IPV6_OCTET_CNT) {
				while (octets.length < CommonCodeUtil.IPV6_OCTET_CNT) {
					tempSipBlock = tempSipBlock.replace(":*:", ":0000:*:");
					octets = tempSipBlock.split(":");
				}
				tempSipBlock = tempSipBlock.replace(":*:", ":0000:");
			}
			if (tempSipBlock.endsWith(":")) {
				tempSipBlock = tempSipBlock.substring(0, tempSipBlock.length() - 1);
			}
			if (tempSipBlock.startsWith(":")) {
				address.append("0000");
				address.append(tempSipBlock);
			} else {
				address.append(tempSipBlock);	
			}
		}
		return address.toString();
	}
	
	public String getShortIpAddress(String sipBlock, String sipVersionTypeCd) {
		//String shortAddr = "";
		StringBuilder address = new StringBuilder();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			String[] octets = sipBlock.split("\\.");
			for (int i = 0; i < octets.length; i++) {
				address.append(String.format("%d", Integer.parseInt(octets[i], 10)));
				if (i < octets.length - 1) {
					address.append(".");
				}
			}
			
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			String[] octets = sipBlock.split(":");
			int maxCnt = 0;
			int startIdx = -1;
			int endIdx = -1;
			for (int i=0; i < octets.length; i++) {
				int val = Integer.valueOf(octets[i], 16);
				octets[i] = String.format("%x", val);
				if (val == 0) {
					if (startIdx == -1) {
						startIdx = i;
						endIdx = i;
						maxCnt = 1;
					} else {
						if (endIdx == (i-1)) {
							endIdx = i;
							int temp = endIdx - startIdx + 1;
							if (temp >= maxCnt) {
								maxCnt = temp;
							}
						} else {
							startIdx = i;
							endIdx = i;
						}
					}
				}
			}
			StringBuilder temp2 = new StringBuilder();
			for (int i=0; i < octets.length; i++) {
				temp2.append(octets[i]);
				if (i < octets.length - 1) {
					temp2.append(":");
				}
			}
			if (maxCnt > 1) {
				StringBuilder pattern = new StringBuilder();
				pattern.append("(:|^)(0+(:|$)){");
				pattern.append(maxCnt);
				pattern.append(",8}");
				address.append(temp2.toString().replaceFirst(pattern.toString(), "::"));
			} else {
				address.append(temp2.toString());
			}
			
		}
		return address.toString();
	}
	
	public String getBinaryIpAddress(String sipBlock, String sipVersionTypeCd) {
		//String binaryAddr = "";
		StringBuilder address = new StringBuilder();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			String[] octects = sipBlock.split("\\.");
			for (int i=0; i < octects.length; i++) {
				int val = Integer.parseInt(octects[i], 10);
				address.append(String.format("%8s", Integer.toBinaryString(val)).replace(" ", "0"));
			}
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
			String[] octects = sipBlock.split(":");
			for (int i=0; i < octects.length; i++) {
				int val = Integer.parseInt(octects[i], 16);
				address.append(String.format("%16s", Integer.toBinaryString(val)).replace(" ", "0"));
			}
		}
		return address.toString();
	}
	
	public boolean isIpPatternValidation(String ipVersion, String ipAddr) {
		if (ipVersion == null || ipAddr == null) {
			return false;
		}
		if (ipVersion.equals(CommonCodeUtil.IPV4) && ipAddr.matches(CommonCodeUtil.IPV4_PATTERN)) {
			return true;
		} else if (ipVersion.equals(CommonCodeUtil.IPV6) && ipAddr.matches(CommonCodeUtil.IPV6_PATTERN)) {
			return true;
		}
		return false;
	}
	
	public String StringXorCalculater(String s1,String s2){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<s1.length() && i<s2.length();i++){
			sb.append((char)(s1.charAt(i) ^ s2.charAt(i)));
		}
		return sb.toString();
	}

	public String StringOrCalculater(String s1,String s2){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<s1.length() && i<s2.length();i++){
			sb.append((char)(s1.charAt(i) | s2.charAt(i)));
		}
		return sb.toString();
	}
	
	public String StringAndCalculater(String s1,String s2){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<s1.length() && i<s2.length();i++){
			sb.append((char)(s1.charAt(i) & s2.charAt(i)));
		}
		return sb.toString();
	}
	
	public String getIpAddressFromDecimal(BigInteger nipaddress,String sipVersionTypeCd ){
		return getIpAddressFromFullBinary(getDecimalToStringBinary(nipaddress, sipVersionTypeCd),sipVersionTypeCd);
	}
	
	public String getIpAddressFromFullBinary(String binary, String sipVersionTypeCd){
		
		String retStr = "";
		StringBuffer sb = new StringBuffer();
		int fixLength = 8;
		int strArraySize = (int) Math.ceil((double)binary.length() / fixLength);
		// int index = 0;

		// String[] subStringArray = new String[strArraySize];

		if(sipVersionTypeCd.equals("CV0001")){
			if(binary.length()==32){
				for(int startIndex = 0; startIndex < binary.length(); startIndex += fixLength) {
					int convInt = Integer.parseInt(binary.substring(startIndex, Math.min(startIndex + fixLength, binary.length())),2);
					if(startIndex==24){
						sb.append(convInt);
//						retStr = retStr + Integer.toString(convInt); //Codeeyes-Urgent-String 추가 시 String Buffer 사용
					}else{
						sb.append(convInt);
						sb.append(".");
//						retStr = retStr + Integer.toString(convInt) + "."; //Codeeyes-Urgent-String 추가 시 String Buffer 사용
					}
					
				}
				
			}
		}else{
			PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
		}
		
		retStr = sb.toString();
		
		return retStr;
	}
		
}
