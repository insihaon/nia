package com.kt.ipms.legacy.cmn.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

public class PrintLogUtil {
	/** LOG 출력 여부 변수 **/
	private static boolean isPrintLog = true;
	/** LOG DEBUG OR INFO 모드 여부 변수 (true : DEBUG, false : INFO) **/
	private static boolean isDebug = false;
	
	/**
	 * VO Class Member 로그 출력.
	 * @param voObject voObject
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void printLogValueObject(Object voObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException { 
		if (isPrintLog) {
			printLog("----------------------------------------------------------------------");
			if (voObject != null) {
				if (voObject instanceof List) {
					printLog("ClassName : " + voObject.getClass().getName());
					List listObject = (List) voObject;
					for (int i=0; i < listObject.size(); i++) {
						printLog("ROW IDX : " + i);
						printLogValueObject(listObject.get(i));
					}
				} else {
					printLog("ClassName : " + voObject.getClass().getName());
					Method[] methods = voObject.getClass().getMethods();
					for (int i=0; i < methods.length; i++) {
						if (methods[i].getName().startsWith("get", 0)) {
							if (methods[i].getName().equals("getClass")) continue;
							Class returnType = methods[i].getReturnType();
							if (returnType != null && returnType.isArray()) {
								Object[] tmpArr = (Object[]) methods[i].invoke(voObject);
								if (tmpArr != null) {
									for (int j=0; j<tmpArr.length; j++) {
										printLog("\t" + methods[i].getName() +" index "+ j +" = "+ tmpArr[j]);
									}
								}
							}
							printLog("\t" +methods[i].getName() +" = ["+ methods[i].invoke(voObject) +"], " 
										+"ReturnType :"+ methods[i].getReturnType().getName());
						}
					}	
				}
			} else {
				printLog("Object IS NULL");
			}
			printLog("----------------------------------------------------------------------");
		}
	}
	
	/**
	 * List<HashMap> 로그 출력.
	 * @param listObject listObject
	 * @throws Exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void printLogListHashMap(List listObject) {
		if (isPrintLog) {
			printLog("----------------------------------------------------------------------");
			if (listObject != null) {
				for (int i=0; i < listObject.size(); i++) {
					printLog("List Row Index : " + i);
					Map mapObject = (Map)listObject.get(i);
					Iterator iter = mapObject.keySet().iterator();
					while (iter.hasNext()) {
						String key = (String) iter.next();
						String val = "null";
						if (mapObject.get(key) != null) {
							val = mapObject.get(key).toString();
							printLog("\tKEY : " + key + ", VALUE : " + val +", TYPE : " 
									+ mapObject.get(key).getClass().getName());
						} else {
							printLog("\tKEY : " + key + ", VALUE : " + val +", TYPE : unknown");
						}
					}
				}
				printLog("List TotalCount : " + listObject.size());
			} else {
				printLog("ListHashMap IS NULL");
			}
			printLog("----------------------------------------------------------------------");
		}
	}
	
	/**
	 * HashMap 로그 출력.
	 * @param mapObject mapObject
	 * @throws Exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void pringLogHashMap(Map mapObject) {
		if (isPrintLog) {
			printLog("----------------------------------------------------------------------");
			if (mapObject != null) {
				Iterator iter = mapObject.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					String val = "null";
					if (mapObject.get(key) != null) {
						val = mapObject.get(key).toString();
						printLog("\tKEY : " + key + ", VALUE : " + val +", TYPE : " 
									+ mapObject.get(key).getClass().getName());
					} else {
						printLog("\tKEY : " + key + ", VALUE : " + val +", TYPE : unknown");
					}
				}
			} else {
				printLog("HashMap IS NULL");
			}
			printLog("----------------------------------------------------------------------");
		}
	}
	
	/**
	 * 로그 메세지 출력 
	 * @param logMessage
	 */
	public static void printLog(String logMessage) {
		KTLogger logger = KTLoggerFactory.getLogger(PrintLogUtil.class);
		if (isDebug) {
			logger.debug(logMessage);
		} else {
			logger.info(logMessage);
		}
	}
	
	public static void printError(Throwable e) {
		KTLogger logger = KTLoggerFactory.getLogger(PrintLogUtil.class);
//		logger.error(e);
		printLogInfo("-------------------------------Error----------------------------------");
		logger.info(e.toString());
		printLogInfo("----------------------------------------------------------------------");
	}
	
	public static void printLogInfo(String logMessage) {
		KTLogger logger = KTLoggerFactory.getLogger(PrintLogUtil.class);
		logger.info(logMessage);
	}
	
	@SuppressWarnings("rawtypes")
	public static void printLogInfoValueObject(Object voObject)  throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		printLogInfo("----------------------------------------------------------------------");
		if (voObject != null) {
			if (voObject instanceof List) {
				printLogInfo("ClassName : " + voObject.getClass().getName());
				List listObject = (List) voObject;
				for (int i=0; i < listObject.size(); i++) {
					printLogInfo("ROW IDX : " + i);
					printLogInfoValueObject(listObject.get(i));
				}
			} else {
				printLogInfo("ClassName : " + voObject.getClass().getName());
				Method[] methods = voObject.getClass().getMethods();
				for (int i=0; i < methods.length; i++) {
					if (methods[i].getName().startsWith("get", 0)) {
						if (methods[i].getName().equals("getClass")) continue;
						Class returnType = methods[i].getReturnType();
						if (returnType != null && returnType.isArray()) {
							Object[] tmpArr = (Object[]) methods[i].invoke(voObject);
							if (tmpArr != null) {
								for (int j=0; j<tmpArr.length; j++) {
									printLogInfo("\t" + methods[i].getName() +" index "+ j +" = "+ tmpArr[j]);
								}
							}
						}
						printLogInfo("\t" +methods[i].getName() +" = ["+ methods[i].invoke(voObject) +"], " 
									+"ReturnType :"+ methods[i].getReturnType().getName());
					}
				}	
			}
		} else {
			printLogInfo("Object IS NULL");
		}
		printLogInfo("----------------------------------------------------------------------");
	}
}
