package com.nia.korenrca.service.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.service.cipher.tea.TEA;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.system.DesktopSystem;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class Utils {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final int startYear = 2008;
    public static final String separator = "/";
    private static boolean isWindows = File.pathSeparator.equals(";");
    private static Boolean dev;

    public static boolean isWindows() {
        return isWindows;
    }

    public static boolean IsDev() {
        if (dev == null) {
            List<String> list = Utils.getLocalServerIp();
            dev = list.contains("192.168.10.2");
        }
        return dev || DesktopSystem.IsDevelopmentMode;
    }

    public static JsonObject createDocument(Data data) {
        JsonObject rootJsonObject = new JsonObject();
        JsonObject rootElement = createJsonObject(data);
        rootJsonObject.put(Constants.JSON_RESULT_NAME, rootElement);
        return rootJsonObject;
    }

    public static JsonObject createDocument(ArrayList<Data> dataList) {
        JsonObject rootJsonObject = new JsonObject();
        JsonArray rootElement = createJsonArray(dataList);
        rootJsonObject.put(Constants.JSON_RESULT_NAME, rootElement);
        return rootJsonObject;
    }

    public static JsonObject createDocument(String dataName1, ArrayList<Data> dataList1, String dataName2, ArrayList<Data> dataList2) {
        JsonObject rootJsonObject = new JsonObject();
        JsonObject dataJsonObject = new JsonObject();
        JsonArray rootElement1 = createJsonArray(dataList1);
        JsonArray rootElement2 = createJsonArray(dataList2);
        dataJsonObject.put(dataName1, rootElement1);
        dataJsonObject.put(dataName2, rootElement2);
        rootJsonObject.put(Constants.JSON_RESULT_NAME, dataJsonObject);

        return rootJsonObject;
    }

    public static JsonArray createJsonArray(ArrayList<Data> dataList) {
        JsonArray rootElement = new JsonArray();
		{
            for (Data data : dataList) {
                JsonObject JsonObject = new JsonObject();

                Iterator<String> keys = data.getProperties().keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = data.get(key);

                    try {
                        if (value instanceof Timestamp) {
                            JsonObject.put(key, ((Timestamp)value).getTime());
                        } else if (value instanceof Date) {
                            JsonObject.put(key, new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(((Date)value)));
                        } else if (value instanceof BigDecimal) {
                            JsonObject.put(key, ((BigDecimal)value).floatValue());
                        } else if (value instanceof org.postgresql.util.PGobject) {
                            String jsonString = value.toString();
                            if(jsonString.startsWith("[")){
                                JsonArray jsonObject = new JsonArray(jsonString);
                                JsonObject.put(key, jsonObject);
                            } else {
                                JsonObject jsonObject = new JsonObject(jsonString);
                                JsonObject.put(key, jsonObject);
                            }
                        } else {
                            JsonObject.put(key, value);
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.ERROR, e.getMessage());
                    }
                }

                rootElement.add(JsonObject);
            }
		}
        return rootElement;
    }

    public static JsonObject createJsonObject(Data data) {
        JsonObject rootJsonObject = new JsonObject();

        if(data != null) {
            Iterator<String> keys = data.getProperties().keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = data.get(key);

                if (value instanceof Timestamp) {
                    rootJsonObject.put(key, ((Timestamp) value).getTime());
                } else if (value instanceof Date) {
                    rootJsonObject.put(key, new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(((Date) value)));
                } else if (value instanceof BigDecimal) {
                    rootJsonObject.put(key, ((BigDecimal) value).floatValue());
                } else if (value instanceof Data) {
                    rootJsonObject.put(key, createJsonObject((Data) value));
                } else if (value instanceof org.postgresql.util.PGobject) {
                    String jsonString = value.toString();
                    if (jsonString.startsWith("[")) {
                        JsonArray jsonObject = new JsonArray(jsonString);
                        rootJsonObject.put(key, jsonObject);
                    } else {
                        JsonObject jsonObject = new JsonObject(jsonString);
                        rootJsonObject.put(key, jsonObject);
                    }
                } else {
                    rootJsonObject.put(key, value);
                }
            }
        }
        return rootJsonObject;
    }

    public static JsonObject httpRequest(String method, String host, int port, String service, String parameter) {
        return httpRequest(method, host, Integer.toString(port), service, parameter);
    }
    public static JsonObject httpRequest(String method, String host, String port, String service, String parameter) {
        return httpRequest(method, host, port, service + parameter);
    }
    public static JsonObject httpRequest(String method, String host, String port, String service) {
        String url = "http://" + host + ":" + port + "/" + service;
        return httpRequest(method, url);
    }
    public static JsonObject httpRequest(String method, String strUrl) {
        JsonObject json = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            bufferedReader.close();

            json = new JsonObject(sb.toString());
            System.out.println(url + ": " + json);
        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().indexOf("refused") == -1) {
                System.out.println("fetchData error:" + e);
            }
            json = new JsonObject().put("error", e.toString());
        }

        return json;
    }

    public static JsonObject httpsRequest(String method, String host, int port, String service, String parameter) {
        return httpsRequest(method, host, Integer.toString(port), service, parameter);
    }
    public static JsonObject httpsRequest(String method, String host, String port, String service, String parameter) {
        String url = "https://" + host + ":" + port + "/" + service;
        return httpsRequest(method, url, parameter);
    }
    public static JsonObject httpsRequest(String method, String strUrl, String parameter) {
        JsonObject json = null;
        try {

            HttpsURLConnection.setDefaultSSLSocketFactory(SSLUtil.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(SSLUtil.getHostnameVerifier());

            URL url = new URL(strUrl);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestMethod(method);
            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(parameter.getBytes("UTF-8"));
            out.flush();
            out.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            bufferedReader.close();

            json = new JsonObject(sb.toString());
            System.out.println(url + ": " + json);
        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().indexOf("refused") == -1) {
                System.out.println("fetchData error:" + e);
            }
            json = new JsonObject().put("error", e.toString());
        } catch (Exception e) {
            json = new JsonObject().put("error", e.toString());
        }

        return json;
    }

    public static JsonObject queryToJSON(String query) throws UnsupportedEncodingException {
		JsonObject json = new JsonObject();
		String[] pairs = query.split("&");
        for (String pair : pairs) {
			int idx = pair.indexOf("=");
			String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            if (!json.containsKey(key)) {
                json.put(key, value);
            } else {
                Object oldValue = json.getValue(key);
                JsonArray array;
                if (oldValue instanceof JsonArray) {
                    array = (JsonArray)oldValue;
                } else {
                    array = new JsonArray();
                    array.add(oldValue);
                    json.put(key, array);
                }
                if (value == null) {
                    array.addNull();
                } else {
                    array.add(value);
                }
            }
        }

        return json;
    }

    public static void jsonstringToData(Data data, String json) {
        try {
            Map map = Utils.convertJsonToClass(json, Map.class);
            if (map != null) {
                data.setProperties(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void jsonstringToData(Data data, String json) {
//		JsonObject jsonObject = new JsonObject(json);
//		if (jsonObject != null) {
//			Iterator<String> keys = jsonObject.getMap().keySet().iterator();
//            while (keys.hasNext()) {
//                String key = keys.next();
//				Object value = jsonObject.getValue(key);
//
//                // System.out.println( String.format("키 : %s, 값 : %s", key, value) );
//
//                try {
//
//                    if (value instanceof String) {
//
//                        String string = (String) value;
//
//                        if (string.startsWith("[")) {
//                            JsonObject obj = new JsonObject("{ array_value:" + string + "}");
//                            JsonArray array = obj.getJsonArray("array_value");
//                            if(array != null){
//                                value = array;
//                            }
//                        } else {
//                            JsonObject obj = new JsonObject(string);
//                            if(obj != null){
//                                value = obj;
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                }
//
//                if (value instanceof JsonObject) {
//					value = jsonToData((JsonObject)value);
//				} else if (value instanceof JsonArray) {
//					value = jsonToData((JsonArray)value);
//				}
//
//                data.set(key, value);
//            }
//        }
//    }
//
//	public static Data jsonToData(JsonObject json) {
//		Data data = null;
//		if (json != null) {
//			data = new Data();
//			Iterator<String> keys = json.getMap().keySet().iterator();
//			while (keys.hasNext()) {
//				String key = keys.next();
//				Object value = json.getValue(key);
//
//				// System.out.println( String.format("키 : %s, 값 : %s", key, value) );
//
//				if (value instanceof JsonObject) {
//					value = jsonToData((JsonObject)value);
//				} else if (value instanceof JsonArray) {
//					value = jsonToData((JsonArray)value);
//				} else if (value instanceof String){
//				    int a = 1;
//                }
//				data.set(key, value);
//			}
//		}
//		return data;
//	}
//
//	public static ArrayList<Object> jsonToData(JsonArray jsonArray) {
//		ArrayList<Object> dataList = null;
//		if (jsonArray != null) {
//			dataList = new ArrayList<>();
//			for (Object value : jsonArray) {
//				if (value instanceof JsonObject) {
//					value = jsonToData((JsonObject)value);
//				} else if (value instanceof JsonArray) {
//					value = jsonToData((JsonArray)value);
//                } else if (value instanceof String){
//                    int a = 1;
//                }
//				dataList.add(value);
//			}
//		}
//		return dataList;
//	}

    public static float getJsonNumber(JsonObject json, String key) {
        if (json != null && json.containsKey(key)) {
            try {
                return ((Number)json.getValue(key)).floatValue();
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static float getJsonNumber(Data data, String key) {
        if (data != null && data.containsKey(key)) {
            try {
                return ((Number)data.get(key)).floatValue();
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static JsonObject createJsonObject(String text) {
        JsonObject json = null;
        try {
            json = new JsonObject(text);
        } catch (Exception ex) {
        }
        return json;
    }

    public static final String getStackTrace(Throwable e) {
        String trace = "";
        String message = "";

        for (StackTraceElement el : e.getStackTrace()) {
            String logText = el.toString();
            trace += "\t" + logText + "\n";
        }

        message += "Message: " + e.getMessage() + "\n";
        message += "StackTrace:\n";
        message += trace;

        // System.out.println(message);

        return message;
    }

	public static void saveImage(InputStream imageStream, String filename, String extension) {
		InputStream inStream = imageStream;

		try {
			String dataString = convertStreamToString(inStream);

			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(dataString);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			// write the image to a file
			File outputfile = new File(filename);
			ImageIO.write(image, extension, outputfile);

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	public static String saveImage(String dataString, String filename, String extension) {

		try {
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(dataString);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			// write the image to a file
			File outputfile = new File(filename);
			ImageIO.write(image, extension, outputfile);
			return outputfile.getAbsoluteFile().toString();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

		return null;
	}

	public static String convertStreamToString(String filename) {
		try {
			Path path = Paths.get(filename);
			byte[] data = Files.readAllBytes(path);
			return javax.xml.bind.DatatypeConverter.printBase64Binary(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertStreamToString(File file) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(file);
		return Utils.convertStreamToString(inputStream);
	}

	@SuppressWarnings("resource") public static String convertStreamToString(InputStream inputStream) throws FileNotFoundException {
		java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String readFile(String filename) {
		File f = new File(filename);
		try {
			byte[] bytes = Files.readAllBytes(f.toPath());
			return new String(bytes, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static double parseDouble(Data data, String key) {
		if (data == null) {
			return 0d;
		}

		Object value = data.get(key);
		if (value == null) {
			return 0d;
		}

		return Double.parseDouble(value.toString());
	}

	public static long parseLong(Data data, String key) {
		if (data == null) {
			return 0;
		}

		Object value = data.get(key);
		if (value == null) {
			return 0;
		}

		return Long.parseLong(value.toString());
	}

    public static String combine(Object... paths) {
        File file = new File(String.valueOf(paths[0]));

        for (int i = 1; i < paths.length; i++) {
            file = new File(file, String.valueOf(paths[i]));
        }

        String path = file.getPath();
        path = replaceSeparator(path);
        return path;
    }

	public static String getExtension(String fileName) {
		char ch;
		int len;
		if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\' || //in the case of a directory
				ch == '.') //in the case of . or ..
		{
			return "";
		}
		int dotInd = fileName.lastIndexOf('.'), sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		} else {
			return fileName.substring(dotInd + 1).toLowerCase();
		}
	}

	public static String getFile(String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			try {
				byte[] bytes = Files.readAllBytes(f.toPath());
				return new String(bytes, "UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		} else {
			StringBuilder result = new StringBuilder("");
			BufferedReader br = null;

			try {
				String sCurrentLine;

				URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
				URLConnection conn = url.openConnection();
				InputStream in = conn.getInputStream();

				br = new BufferedReader(new InputStreamReader(in));
				while ((sCurrentLine = br.readLine()) != null) {
					result.append(sCurrentLine).append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			return result.toString();
		}
	}

    public static String replaceSeparator(String path) {
        return path != null && isWindows() ? path.replace("\\", Utils.separator) : path;
    }

    public static String getPropertiesPath() {
        String filePath = Constants.PROPERTIES.PATH;
        return filePath;
    }

    public static String getPropertiesFile() {
        String fileName = getPropertiesPath() + Constants.PROPERTIES.CONFIG_FILENAME;
        return fileName;
    }

    public static void doRedirect(HttpServerResponse response, String url) {
        response.putHeader("location", url).setStatusCode(302).end();
    }

    public static Map<String, Object> splitQuery(String parameters) {
		Map<String, Object> query_pairs = new HashMap<>();
        try {
            String query = parameters;
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                try {
                    query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
		}
        return query_pairs;
    }

    public static ArrayList<String> getLocalServerIp() {
        ArrayList<String> endPoints = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        endPoints.add(inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return endPoints;
    }

    private static ObjectMapper mapper = new ObjectMapper();
    public static <T> T convertJsonToClass(String json, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        return mapper.readValue(json, valueType);
    }

    public static <T> List<T> convertJsonToListClass(String json, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        List<T> list = Arrays.asList(mapper.readValue(json, valueType));
        return list;
    }

    public static <T> List<T> convertJsonToListClass2(String json, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
        List<T> list = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, valueType));
        return list;
    }

    public static String convertClassToJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static String convertClassToJsonStringPretty(Object object) throws JsonProcessingException {
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return json;
    }

    public static byte[] convertClassToJsonBytes(Object object) throws JsonProcessingException {
        return mapper.writeValueAsBytes(object);
    }

    private static Gson gson = new Gson();
    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

    public static String mkDirs(String path) {

        File file = new File(path);

        if (file.isFile()) {
            path = Utils.replaceSeparator(file.getParent());
            file = file.getParentFile();
        }

        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("디렉토리를 생성했습니다. :" + path);
				return path;
            } else {
                System.out.println("디렉토리를 생성하지 못했습니다. :" + path);
            }
        }
        return null;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String encrypt(String value) {
        String uuid = TEA.generateUUID();
        TEA tea = new TEA(uuid);
        return uuid + tea.encrypt(value);
    }

    public static void DeleteOldFiles(String directory) {

        if(directory == null)
            return;

        Calendar cal = Calendar.getInstance();    // Calendar 객체 생성
        long todayMil = cal.getTimeInMillis();    // 현재 시간(밀리 세컨드)
        long oneDayMil = 24 * 60 * 60 * 1000;    // 일 단위

        Calendar fileCal = Calendar.getInstance();
        Date fileDate = null;

        File path = new File(directory);
        File[] list = path.listFiles();         // 파일 리스트 가져오기

        for (int j = 0; j < list.length; j++) {

            // 파일의 마지막 수정시간 가져오기
            fileDate = new Date(list[j].lastModified());

            // 현재시간과 파일 수정시간 시간차 계산(단위 : 밀리 세컨드)
            fileCal.setTime(fileDate);
            long diffMil = todayMil - fileCal.getTimeInMillis();

            // 시간으로 계산
            int diffDay = (int)(diffMil / (oneDayMil / 24));

            // 1시간이 지난 파일 삭제
            if (diffDay >= 1 && list[j].exists()) {
                list[j].delete();
//                System.out.println(list[j].getName() + " 파일을 삭제했습니다.");
            }
        }
    }

    public static String getCurrentTime() {
        return getCurrentTime(null);
    }

    public static String getCurrentTime(DateFormat dateFormat) {

        if (dateFormat != null) {
            return dateFormat.format(new Date());
        } else {
            return Constants.DATE_FORMAT.MINIMAL_YYYYMMDDMMSS.format(new Date());
        }
    }

    public static String excutePost(String targetURL, String urlParameters) {

/*
        파라미터 넘기는 법
        try {
            String urlParameters = "fName=" + URLEncoder.encode("???", "UTF-8") + "&lName=" + URLEncoder.encode("???", "UTF-8");
        } catch (Exception e) {
        }
*/
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String post(String url, String param) throws Exception {

        String charset = "UTF-8";
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(param.getBytes(charset));
            output.flush();

            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } finally {
        }

        return null;
    }

    public static String httpPostNiaLogin(String UrlData, String ParamData) {
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        URL url = null;
        HttpURLConnection conn = null;

        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        String returnData = "";

        try {
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
            conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
            try (OutputStream os = conn.getOutputStream()){
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            conn.connect();
            LOGGER.log(Level.INFO, "=============> " +  UrlData + "{body: " + ParamData + "}");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입
            returnData = sb.toString();

            //http 요청 응답 코드 확인
            LOGGER.log(Level.INFO,returnData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return returnData;
        }
    }


    public static boolean deleteFile(String pathname) {
		try {
			File file = new File(pathname);
			if (file.delete()) {
				return true;
			}

		} catch (Exception e) {
		}

		return false;
	}

	public static String getDeviceName(int mydevice) {
		String deviceName;
		switch (mydevice) {
			case 10:
				deviceName = "mobile";
				break;
			case 20:
				deviceName = "tablet";
				break;
			case 0:
			default:
				deviceName = "desktop";
				break;
		}

		return deviceName;
	}

	public static int getMyDevice(String deviceName) {
		int mydevice;
		switch (deviceName) {
			case "mobile":
				mydevice = 10;
				break;
			case "tablet":
				mydevice = 20;
				break;
			case "desktop":
			default:
				mydevice = 0;
				break;
		}

		return mydevice;
	}

	public static List<String> Find(String path, String prefixFileName) {
		File scannedDir = new File(path);
		List<String> files = new ArrayList<String>();
		for (File file : scannedDir.listFiles()) {
			files.addAll(Find(file, prefixFileName));
		}
		return files;
	}

	private static List<String> Find(File file, String prefixFileName) {
		List<String> files = new ArrayList<String>();
		String resource = file.getPath();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				files.addAll(Find(child, prefixFileName));
			}
		} else if (resource.indexOf(prefixFileName) != -1) {
			files.add(resource);
		}
		return files;
	}

    public static String format(String s, Object... var2) {
        int i = 0;
        while(s.contains("{}")) {
            s = s.replaceFirst(Pattern.quote("{}"), "{"+ i++ +"}");
        }
        return MessageFormat.format(s, var2);
    }


}
