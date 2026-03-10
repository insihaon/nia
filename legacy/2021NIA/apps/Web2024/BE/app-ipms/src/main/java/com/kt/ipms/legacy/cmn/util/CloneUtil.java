package com.kt.ipms.legacy.cmn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kt.ipms.legacy.cmn.vo.IpVo;

public class CloneUtil {

	/**
	 * Serialization Object Clone 
	 * @param obj obj
	 * @return Object
	 * @throws Exception Exception
	 */
	public static Object createCloneViaSerialization(Serializable obj) throws IOException, ClassNotFoundException {
		Object objCopy = null;
		ByteArrayOutputStream bytes = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
//			bytes = new ByteArrayOutputStream() {
//				public synchronized byte[] toByteArray() {
//					return buf;
//				}
//			};
			bytes = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bytes);
			out.writeObject(obj);

			Set whitelist = new HashSet < String > (Arrays.asList(new String[] {
				
			}));
				    
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes.toByteArray());
			WhitelistObjectInputStreamUtil ois = new WhitelistObjectInputStreamUtil(bais, whitelist);
			objCopy = ois.readObject();
			
//			in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
//			objCopy = in.readObject();
			
		} finally {
			if (bytes != null) {
				bytes.close();
			}
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
		return objCopy;
	}
	
	/**
	 * HashMap Clone 
	 * @param map map
	 * @return Map<String, Object>
	 * @throws Exception Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> createCloneViaMap(Map<String, Object> map) {
		HashMap<String, Object> bind = (HashMap<String, Object>) map;
		return (Map<String, Object>)bind.clone();
	}
	
	/**
	 * ListMap Clone 
	 * @param listMap listMap
	 * @return List<Map<String, Object>>
	 * @throws Exception Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> createCloneViaListMap(List<Map<String, Object>> listMap) {
		ArrayList<Map<String, Object>> bind = (ArrayList<Map<String,Object>>) listMap;
		return (List<Map<String, Object>>)bind.clone();
	}
	
	
	/**
	 * @param srcObject
	 * @param destObject
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void copyObjectInformation(Object srcObject, Object destObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method[] srcMethods = srcObject.getClass().getMethods();
		for (int i=0; i < srcMethods.length; i++) {
			if (srcMethods[i].getName().startsWith("get", 0)) {
				if (srcMethods[i].getName().equals("getClass")) {
					continue;
				}
				Class returnType = srcMethods[i].getReturnType();
				String setMethodName = "set" + srcMethods[i].getName().substring(3);
				try {
					Method destMethod = destObject.getClass().getMethod(setMethodName, returnType);
					destMethod.invoke(destObject, srcMethods[i].invoke(srcObject));
				} catch (NoSuchMethodException e) {
					continue;
				} 
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void copyIpVoInfomation(IpVo srcIpVo, IpVo destIpVo) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method[] methods = IpVo.class.getDeclaredMethods();
		for (int i=0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get", 0)) {
				Class returnType = methods[i].getReturnType();
				String setMethodName = "set" + methods[i].getName().substring(3);
				Method srcMethod = srcIpVo.getClass().getMethod(methods[i].getName());
				Method destMethod = destIpVo.getClass().getMethod(setMethodName, returnType);
				destMethod.invoke(destIpVo, srcMethod.invoke(srcIpVo));
			}
		}
	}
	
	public static List<Map<String, Object>> convertListVoToListMap(List<?> voList) {
		List<Map<String, Object>> resultListMap = null;
		if (voList == null || voList.size() == 0) {
			return resultListMap;
		}
		
		resultListMap = new ArrayList<Map<String,Object>>();
		for (Object orgVo : voList) {
			Method[] methods = orgVo.getClass().getMethods();
			Map<String, Object> itemMap = new HashMap<String, Object>();
			for (Method method : methods) {
				if (method.getName().startsWith("get", 0)) {
					if (method.getName().equals("getClass")) {
						continue;
					}
					try {
						itemMap.put(method.getName(), method.invoke(orgVo));
					} catch (Exception e) {
						itemMap.put(method.getName(), null);
					}
				}
			}
			resultListMap.add(itemMap);
		}
		return resultListMap;
	}
	
	

}
