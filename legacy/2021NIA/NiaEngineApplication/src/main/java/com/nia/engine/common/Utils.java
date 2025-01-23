package com.nia.engine.common;



import com.nia.engine.service.impl.EngineClearSchedulerJobServiceImpl;
import org.apache.log4j.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Utils {

    public static final int startYear = 2008;
    public static final String separator = "/";
    private static boolean isWindows = File.pathSeparator.equals(";");
    private static Boolean dev;
    private static final Logger LOGGER = Logger.getLogger(EngineClearSchedulerJobServiceImpl.class);


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
            LOGGER.info("=============> " +  UrlData + "{body: " + ParamData + "}");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입
            returnData = sb.toString();

            //http 요청 응답 코드 확인
            LOGGER.info(returnData);
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




}
