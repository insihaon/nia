package com.codej.base.utils;
// package com.codej.base.utils;

// import java.io.IOException;
// import java.util.HashMap;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import com.google.gson.Gson;
// import com.google.gson.JsonArray;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;

// import lombok.extern.slf4j.Slf4j;
// import okhttp3.FormBody;
// import okhttp3.OkHttpClient;
// import okhttp3.Request;
// import okhttp3.RequestBody;
// import okhttp3.Response;

// @Slf4j
// @Component
// public class ApiUtil {
//     @Value("${myconf.api-server.hub-api:http://10.220.178.184:28000}")
//     private static String REQUEST_API_DATA_SET;

//     private static final OkHttpClient client = new OkHttpClient();

//     private static String getRequestApiUrl() {
//         return REQUEST_API_DATA_SET == null ? "http://10.220.178.184:28000" : REQUEST_API_DATA_SET;
//     }

//      public static String requestApiDataSetService(String apiUrl, HashMap<String, Object> jsonData) throws IOException {
//         // HashMap을 JSON으로 직렬화
//         Gson gson = new Gson();
//         String dataSetJson = gson.toJson(jsonData);

//         RequestBody body = new FormBody.Builder()
//                 .add("jsonData", dataSetJson)
//                 .build();

//         Request request = new Request.Builder()
//                 .url(getRequestApiUrl() + apiUrl)
//                 .post(body)
//                 .build();

//         Response response = client.newCall(request).execute();
//         String responseBody = response.body().string();

//         JsonParser jsonParser = new JsonParser();
//         JsonElement root = jsonParser.parse(responseBody);
    
//         if (root.isJsonObject()) {
//             JsonObject jsonObject = root.getAsJsonObject();
//             JsonArray data = jsonObject.getAsJsonArray("data");
    
//             if (data.size() > 0) {
//                 JsonObject responseDataSet = data.get(0).getAsJsonObject(); // Assuming the first ACL group
//                 String result = responseDataSet.getAsString();
//                 return result;
//             }
//         }

//         return null;
        
//     }                                                                                           
// }
