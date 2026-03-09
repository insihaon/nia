package com.nia.korenrca.server.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import com.nia.korenrca.excel.ExcelUtil;
import com.nia.korenrca.server.scheduler.CronTriggerRunner;
import com.nia.korenrca.server.worker.EventProxyServer;
import com.nia.korenrca.service.base.BaseDbService;
import com.nia.korenrca.service.util.Mail;
import com.nia.korenrca.service.util.SSHUtil;
import com.nia.korenrca.service.util.SSLUtil;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.DataSourceType;
import com.nia.korenrca.shared.Request;
import com.nia.korenrca.shared.Response;
import com.nia.korenrca.shared.properties.CommonProperties;
import com.nia.korenrca.shared.properties.MapperProperties;

import io.vertx.core.json.JsonObject;

public class RCAService extends BaseDbService {
    private final SdnTokenService sdnTokenService;

    public RCAService() {
        super(DataSourceType.RCADB.toString());
        sdnTokenService = new SdnTokenService();
    }

    @Override
    public ArrayList<String> getMapperResources() {
        ArrayList<String> list = super.getMapperResources();
        list.add(MapperProperties.RCASqlMap);
        list.add(MapperProperties.RCASqlMapRoadshow);
        return list;
    }

    @Override
    protected Response doCustomRequest(Request request) {
        Response response = null;

        switch (getAction()) {
            case Constants.NIA.ACTION.SELECT_CURRENT_TIME:
                response = getDbTime();
                break;
            case Constants.NIA.ACTION.SELECT_RCA_ALARM:
                response = getAlarm();
                break;
            case Constants.NIA.ACTION.GET_TOPOLOGY:
                response = getToplogy();
                break;
            case Constants.NIA.ACTION.REQUEST_3D_TOPLOGY:
                response = request3DToplogy();
                break;
            case Constants.NIA.ACTION.CHANGE_TICKET_STATUS:
                response = sendToEngine();
                break;
            case Constants.NIA.ACTION.CHANGE_SYSLOG_STATUS:
                response = sendToEngine();
                break;
            case Constants.NIA.ACTION.EXPORT_EXCEL:
                response = exportExcel();
                break;
            case Constants.NIA.ACTION.GET_TICKET_ALL:
                response = getTicketAll();
                break;
            case Constants.NIA.ACTION.UPDATE_TICKET_TOPOLOGY:
            case Constants.NIA.ACTION.DELETE_TICKET_TOPOLOGY:
                response = updateTicketTopology();
                break;

            case Constants.NIA.ACTION.GET_DEFAULT_CHART:
                response = getSavedChartData();
                break;
            case Constants.NIA.ACTION.UPDATE_USER_GRANT_LIST:
                response = getSavedlvldata();
                break;
            case Constants.NIA.ACTION.SELECT_EMAIL:
                response = sendEmail();
                break;
            case Constants.NIA.ACTION.SEND_PING:
                response = sendPing();
                break;
            case Constants.NIA.ACTION.REQUEST_RESTFUL:
                response = requestRest();
                break;
            case Constants.NIA.ACTION.REQUEST_HTTPS_RESTFUL:
                response = requestHttpsRest();
                break;
            case Constants.NIA.ACTION.REQUEST_POST:
                response = requestPost();
                break;
            case Constants.NIA.ACTION.REQUEST_SIMULATOR:
                response = requestSimulator();
                break;
            case Constants.NIA.ACTION.RESET_TICKET:
                response = resetTicket();
                break;
            case Constants.NIA.ACTION.REQUEST_DATA_SNAPSHOT:
                response = sendToEngine();
                break;
            case Constants.NIA.ACTION.REQUEST_LOGIN_NIA:
                response = requestPostNiaLogin();
                break;
            case Constants.NIA.ACTION.REQUEST_NIA_API:
                response = requestNiaApi();
                break;
            case Constants.NIA.ACTION.REQUEST_IPSDN_API:
                response = requestSdnApi();
                break;
            case "FOREACH_SYNTAX_TEST":
            // Data param = getData();
            break;
            default:
                response = runCommand();
                break;
        }

        return response;
    }

    private Response getTicketAll() {
        try {

            JsonObject rootJsonObject = new JsonObject();
            ArrayList<Data> result2 = selectListData(Constants.NIA.SQL.SELECT_TICKET_CUR_LIST, getData());
            rootJsonObject.put(CommonProperties.RESULT2, Utils.createJsonArray(result2));

            return getRequest().createResponse(rootJsonObject);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }



    private Response getDbTime() {
        try {

/*
            JsonObject rootJsonObject = new JsonObject();
            Data data = selectOneData(Constants.NIA.SQL.SELECT_RCA_ALARM, getData());
            rootJsonObject.put(CommonProperties.LIST1, Utils.createJsonObject(nodeList));
*/
            Data param = getData();
            ArrayList<HashMap<String, Object>> selectList = selectList(Constants.NIA.SQL.SELECT_CURRENT_TIME, param);
            ArrayList<Data> resultList = convertResultData(selectList);
            return getRequest().createResponse(Utils.createDocument(resultList));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response getSavedlvldata() {
        try {
            Data param = getData();
            update("UPDATE_USER_GRANT_LIST",param);
            commit();
            ArrayList<HashMap<String, Object>> selectList = selectList("SELECT_USER_LIST", null);
            ArrayList<Data> resultList = convertResultData(selectList);
            return getRequest().createResponse(Utils.createDocument(resultList));
        } catch (Throwable e) {
            rollback();
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response request3DToplogy() {
        try {
            Data param = getData();
            JsonObject rootJsonObject = new JsonObject();

            if (param.get("TICKET_ID") == null) {
                String msg = "TICKET_ID is null".concat(param.toString());
                logger.log(Level.SEVERE, msg);
                return getRequest().createResponse(msg);
            }

            String saved = param.get("saved");

            HashMap<String, Object> savedToplogy = selectOne(Constants.NIA.SQL.SELECT_TICKET_TOPOLOGY, param);
            if ( Boolean.parseBoolean(saved) == true || (saved == null && savedToplogy != null)) {
                Data data = new Data(savedToplogy);
                data.set("IS_SAVED", true);
                return getRequest().createResponse(Utils.createJsonObject(data));
            }

            Data ticket = null;
            if (param.get("CLUSTER_NO") == null) {
                param.set("LIMIT", 1);
                ticket = selectFirstData(Constants.NIA.SQL.SELECT_TICKET_CUR_LIST, param);
                if (ticket != null && ticket.getMap() != null) {
                    param.set("CLUSTER_NO", ticket.get("cluster_no"));
                    rootJsonObject.put("TICKET", Utils.createJsonObject(ticket));
                }
            }

            ArrayList<Data> resultList = new ArrayList<>();
            ArrayList<Data> crossLinkList = new ArrayList<>();

            if (saved != null && Boolean.parseBoolean(saved) == true) {
                rootJsonObject.put(CommonProperties.LIST, Utils.createJsonArray(resultList));
                return getRequest().createResponse(rootJsonObject);
            }

            ArrayList<HashMap<String, Object>> selectList = selectList("SELECT_TICKET_MAP_3D_LIST", param);

            boolean hasRoadm = false;

            for(HashMap<String, Object> one : selectList)
            {
                Data data = new Data(one);
                if (one.getOrDefault("equiptypea", "").toString().startsWith(Constants.NIA.EQUIP_TYPE.ROADM) || one.getOrDefault("equiptypez", "").toString().startsWith(Constants.NIA.EQUIP_TYPE.ROADM)) {

                    hasRoadm = true;

                    one.put("CLUSTER_NO", param.get("CLUSTER_NO"));

                    ArrayList<Data> arrLeft = selectListData("SELECT_ROADM_PROP_EQUIP_LEFT", one);
                    if (arrLeft.size() > 0) {
                        data.set("PROP_EQUIP_LEFT", arrLeft);

                        for (Data prop : arrLeft) {
                            makeCrossLink(selectList, prop, crossLinkList);
                        }
                    }

                    ArrayList<Data> arrRight = selectListData("SELECT_ROADM_PROP_EQUIP_RIGHT", one);
                    if (arrRight.size() > 0) {
                        data.set("PROP_EQUIP_RIGHT", arrRight);

                        for (Data prop : arrRight) {
                            makeCrossLink(selectList, prop, crossLinkList);
                        }
                    }

                    makeCrossLink(selectList, one, crossLinkList);
                }

                resultList.add(data);
            }

            if (hasRoadm == false) {

                for (HashMap<String, Object> one : selectList) {
                    if (one.getOrDefault("equiptypea", "").toString().startsWith(Constants.NIA.EQUIP_TYPE.FDF) || one.getOrDefault("equiptypez", "").toString().startsWith(Constants.NIA.EQUIP_TYPE.FDF)) {
                        makeCrossLink(selectList, one, crossLinkList);
                    }
                }
            }

            rootJsonObject.put(CommonProperties.LIST, Utils.createJsonArray(resultList));
            ArrayList<Data> deduplication = new ArrayList<Data>(new HashSet<Data>(crossLinkList));
            rootJsonObject.put("CROSS_LINK", Utils.createJsonArray(deduplication));
            return getRequest().createResponse(rootJsonObject);

        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private static HashMap<String, Integer> map_ticket_status = null;
    private Integer getTicketStatusToNumber(String status) {
        if(map_ticket_status  == null)
        {
            map_ticket_status = new HashMap<String, Integer>() {
                {
                    put("RERV", 0);
                    put("INIT", 1);
                    put("ACK", 2);
                    put("FIX", 3);
                    put("FIN", 4);
                }
            };
        }

        Integer value =  map_ticket_status.get(status);

        return value == null ? -1 : value;
    }
    private Response sendToEngine() {
        try {
            Data param = getData();
            JsonObject json = new JsonObject(param.getMap());
            EventProxyServer.emit(json);

            /*Data param = getData();
            Data data = selectOneData(Constants.NIA.SQL.SELECT_TICKET_CUR_LIST, param);

            logger.log(Level.WARNING, param.toString());

            if (data != null) {
                String strCurrentStatus = data.get("status", "");
                String strNewStatus = param.get("STATUS", "");
                int nCurrentStatus = getTicketStatusToNumber(data.get("status", ""));
                int nNewStatus = getTicketStatusToNumber(strNewStatus);
                if (nCurrentStatus >= nNewStatus && (strNewStatus.equals("INIT") && strCurrentStatus.equals("ACK")) == false && (strNewStatus.equals("ACK") && strCurrentStatus.equals("ACK")) == false)
                {
                    throw new Exception("해당 티켓의 상태가 " + nCurrentStatus + " 입니다.");
                }

                switch (strNewStatus) {
                    case "RERV":    *//*예약*//*
                        break;
                    case "INIT":    *//*생성*//*
                    case "ACK":     *//*인지*//*
                    case "FIX":     *//*조치*//*
                    case "FIN":     *//*마감*//*
                        update("UPDATE_TICKET_CURRENT", param);
                        update("MERGE_HANDLE_STATUS_CURRENT", param);
                        insert("INSERT_STATUS_HIST", param);
                        break;
                    default:
                        throw new Exception("요청한 티켓 상태를 알 수 없습니다. " + strNewStatus + " 입니다.");
                }
            }
            commit();
            data.set("status",param.get("STATUS", ""));
            if(param.get("RCA_ACCURACY", null) != null){
                data.set("rca_accuracy",param.get("RCA_ACCURACY", ""));
            }
            if(param.get("HANDLING_CONTENT", null) != null){
                data.set("handling_content",param.get("HANDLING_CONTENT", ""));
            }
            if(param.get("HANDLING_FIN_CONTENT", null) != null){
                data.set("handling_fin_content",param.get("HANDLING_FIN_CONTENT", ""));
            }
            data.set("is_new_ticket",false);

            JsonObject newTicketData = new JsonObject().put("data",
                new JsonArray().add(new JsonObject(data.getMap())));

            try {
                EventBusBridge.getServer().publish(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET, newTicketData);
            } catch (NullPointerException | IllegalStateException e) {
                EventBusBridge.connectionFailed();
            }*/

            Data ret = new Data();
            ret.set(Constants.JSON_RESULT_NAME, true);
            return getRequest().createResponse(Utils.createJsonObject(ret));
        } catch (Throwable e) {
            rollback();
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response exportExcel(){
        try {
            Data param = getData();

            ArrayList<HashMap<String, Object>> selectList = selectList(param.get("sqlId"), param);
            String path = ExcelUtil.createXlsx(selectList, (List) param.get("FIELD"), (List) param.get("FIELDNAMES"));

            Data ret = new Data();
            if(selectList.size() > 0) { ret.set("excelUrl",path); }
            ret.set(Constants.JSON_RESULT_NAME, true);
            return getRequest().createResponse(Utils.createJsonObject(ret));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private void makeCrossLink(ArrayList<HashMap<String, Object>> selectList, HashMap<String, Object> one, ArrayList<Data> crossLinkList) {
        for ( HashMap<String, Object> k : selectList )
        {
            if(one == k)
                continue;

            String locationA = (String) one.get("instlocationa");
            String locationZ = (String) one.get("instlocationz");
            if (locationA != null) {
                if (locationA.equals(k.get("instlocationa"))) {
                    addCrossLinkData(crossLinkList, one.get("sysnamea"), k.get("sysnamea"), k.get("equiptypea"));
                }
                if (locationA.equals(k.get("instlocationz"))) {
                    addCrossLinkData(crossLinkList, one.get("sysnamea"), k.get("sysnamez"), k.get("equiptypez"));
                }
            }
            if (locationZ != null) {
                if (locationZ.equals(k.get("instlocationa"))) {
                    addCrossLinkData(crossLinkList, one.get("sysnamez"), k.get("sysnamea"), k.get("equiptypea"));
                }
                if (locationZ.equals(k.get("instlocationz"))) {
                    addCrossLinkData(crossLinkList, one.get("sysnamez"), k.get("sysnamez"), k.get("equiptypez"));
                }
            }
        }
    }

    private void makeCrossLink(ArrayList<HashMap<String, Object>> selectList, Data prop, ArrayList<Data> crossLinkList) {
        String instlocation = prop.get("instlocation");
        if (instlocation != null) {

            for ( HashMap<String, Object> k : selectList )
            {

//                if("북광주-PTS-0608-01-02".equals(k.get("sysnamea")) || "북광주-PTS-0608-01-02".equals(k.get("sysnamez")) )
//                {
//                    if("GJBGB0665".equals(instlocation)){
//                        int a = 1;
//                    }
//                }

                if (instlocation.equals(k.get("instlocationa"))) {
                    addCrossLinkData(crossLinkList, prop.get("prop_sysname"), k.get("sysnamea"), k.get("equiptypea"));
                }
                if (instlocation.equals(k.get("instlocationz"))) {
                    addCrossLinkData(crossLinkList, prop.get("prop_sysname"), k.get("sysnamez"), k.get("equiptypez"));
                }
            }
        }
    }

    private Data addCrossLinkData(ArrayList<Data> crossLinkList, Object sysnamea, Object sysnamez) {
        return addCrossLinkData(crossLinkList, sysnamea, sysnamez, null);
    }

    private Data addCrossLinkData(ArrayList<Data> crossLinkList, Object sysnamea, Object sysnamez, Object type) {

        if(sysnamea != null && sysnamez != null && sysnamea.equals(sysnamez))
            return null;

        Data newLink = new Data();
        newLink.set("sysnamea", sysnamea);
        newLink.set("sysnamez", sysnamez);
        if (type != null) {
            newLink.set("equiptype", type);
        }

        crossLinkList.add(newLink);
        return newLink;
    }

    private Response getToplogy() {
        try {
            Data param = getData();
            ArrayList<HashMap<String, Object>> selectList = selectList("SELECT_TT_LIST", param);

            ArrayList<Data> resultList = new ArrayList<>();
            for(HashMap<String, Object> result : selectList)
            {
                String code =  result.getOrDefault("RCA_CODE", "").toString().toLowerCase();
                switch (code)
                {
                    case "cablecut" :
                        if(result.get("TOPOLOGY_GB") != null){
                            ArrayList<Data> list = selectListData("SELECT_RESULT_LINKCUT_LIST", result);
                            resultList.addAll(list);
                        }
                        break;
                    case "nodefail" :
                        ArrayList<Data> list = selectListData("SELECT_RESULT_NODEFAIL_LIST", result);
                        resultList.addAll(list);
                        break;
                    default:
                        break;
                }

            }

            JsonObject rootJsonObject = new JsonObject();
            rootJsonObject.put(CommonProperties.LIST, Utils.createJsonArray(resultList));
            return getRequest().createResponse(rootJsonObject);

        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response getAlarm() {
        try {
            Data param = getData();
            ArrayList<Data> list = null;
            if("rca".equals(param.get("SYSTEM_GB","").toLowerCase())){

                String id = param.get("ALARM_ID");
                if(id != null){
                    param.set("ALARM_ID", Integer.parseInt(id) );
                }
                list = selectListData(Constants.NIA.SQL.SELECT_RCA_ALARM, param);
            } else {
                list = selectListData(Constants.NIA.SQL.SELECT_MASTER_ALARM, param);
            }
            JsonObject rootJsonObject = new JsonObject();
            rootJsonObject.put(CommonProperties.LIST, Utils.createJsonArray(list));
            return getRequest().createResponse(rootJsonObject);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        } finally {
            close();
        }
    }

    private Response updateTicketTopology() {
        try {

            Response response = runCommand();
            boolean result = response.isSucceeded();

            if (result) {
                broadcastEvent(Constants.SocketEventType.TICKET_TOPOLOGY_CHANGE, getData().getProperties());
            }

            return response;
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response sendEmail(){
        try {
            Data param = getData();
            JsonObject json = new JsonObject();

            boolean data = Mail.sendMail(param, "#KEY#");
//
            if(data){
                json.put("eventType", "REQUEST_CHANGE_TICKET_STATUS");
                json.put("status", "ACK");
                json.put("ticket_id", param.getString("TICKET_ID"));
                json.put("ticket_type", param.getString("TICKET_TYPE"));
                json.put("ticket_result", param.getString("TICKET_RESULT"));
                json.put("detail", "DETAIL");
                json.put("user_ids", param.getString("USER_IDS"));
                json.put("mail_content", param.getString("BODY"));
                json.put("fault_classify", param.getString("FAULT_CLASSIFY"));
                json.put("fault_type", param.getString("FAULT_TYPE"));
                json.put("fault_detail_content", param.getString("FAULT_DETAIL_CONTENT"));
                json.put("etc_content", param.getString("ETC_CONTENT"));
                json.put("handling_ack_user", param.getString("HANDLING_ACK_USER"));

                EventProxyServer.emit(json);
            }

            json.put("send_result",data);

//            json.put("send_result", true);
            return getRequest().createResponse(json);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response sendPing(){
        Data param = getData();
        String ip = param.getString("IP");
        String user_id = param.getString("USER_ID");
        String result = null;
        try {
            logger.log(Level.WARNING, "==========> START SEND PING " + ip);
            result = SSHUtil.ping(ip, user_id);

            JsonObject json = new JsonObject();
            json.put("result", result);
            return getRequest().createResponse(json);
        } catch (Throwable e) {
            logger.log(Level.WARNING, "==========> Failed SEND PING ERROR" + ip, e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }


    private void broadcastEvent(String strEventType, Object data) {
        try {
            JsonObject json = new JsonObject();
            json.put("event_type", strEventType);
            json.put("data", data);

            EventBusBridge eventBusBridge = EventBusBridge.getServer();
            if(eventBusBridge != null){
                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET, json);
            }
        } catch (NullPointerException | IllegalStateException e) {
            EventBusBridge.connectionFailed();
        }
    }

    private Response requestRest() {
        try {
            Data param = getData();
            JsonObject result = Utils.httpRequest(
                param.get("METHOD"),
                param.get("HOST"),
                param.get("PORT"),
                param.get("SERVICE"),
                param.get("PARAM")
//                Utils.convertClassToJsonString(param.get("PARAM"))
            );

//            param.set(Constants.INPUT, new Data(param.get("PARAM")));
            return getRequest().createResponse(result);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }
    private Response requestHttpsRest() {
        try {
            Data param = getData();
            JsonObject result = Utils.httpsRequest(
                param.get("METHOD"),
                param.get("HOST"),
                param.get("PORT"),
                param.get("SERVICE"),
                Utils.convertClassToJsonString(param.get("PARAM"))
            );

            param.set(Constants.INPUT, new Data(param.get("PARAM")));
            return getRequest().createResponse(result);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response requestPost() {
        try {
            Data param = getData();
            String result = Utils.post(param.get("URL"), param.get("PARAM"));

            param.set(Constants.INPUT, new Data(param.get("PARAM")));
            return getRequest().createResponse(result);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response requestPostNiaLogin() {
        try {
            String url = "http://203.255.249.31:8088/ipsdn/auth/login";
            String param = "{ \"loginid\" : \"codej\", \"password\" : \"codej!@#\" }";

            String result = Utils.httpPostNiaLogin(url, param);
            JsonObject json = new JsonObject();
            json.put("result", result);
            return getRequest().createResponse(json);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response requestSimulator() {
        try {
            Data param = getData();
            JsonObject result = null;
            try {
                String strUrl = "https://" + ConfigProperties.get().getSimulatorHost() + ":" + ConfigProperties.get().getSimulatorPort() + "/" + param.get("SERVICE");
                URL url = new URL(strUrl);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestProperty("content-type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                connection.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                connection.setSSLSocketFactory(SSLUtil.getSocketFactory(
                    new File(ConfigProperties.get().getSimulatorKeyStorePath()), ConfigProperties.get().getSimulatorKeyStorePassword()
                ));

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                out.writeBytes(Utils.convertClassToJsonString(param.get("PARAM")));
                out.write(Utils.convertClassToJsonString(param.get("PARAM")).getBytes("UTF-8"));
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


                result = (sb.toString() == null || sb.toString().equals("")) ? new JsonObject() : new JsonObject(sb.toString());
                System.out.println(url + ": " + result);
            } catch(Exception e) {
                result = new JsonObject().put("error", e.toString());
            }

            param.set(Constants.INPUT, new Data(param.get("PARAM")));
            return getRequest().createResponse(result);
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response requestNiaApi() {
        JsonObject result = null;
        try {
            Data param = getData();
            String method = param.get("METHOD");
            String service = param.get("SERVICE");
            String parameter = param.get("PARAM");
            String token = param.get("TOKEN");

            String strUrl = "http://" + ConfigProperties.get().getNiaApiHost() + ":" + ConfigProperties.get().getNiaApiPort() + "/" + service + parameter;
            // String strUrl = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/noshut?nodename=daejeon-5812&ifname=xe13";
            // String strUrl = "http://203.255.249.31:8088/ipsdn/services/config/interfaces/shutdown?nodename=daejeon-5812&ifname=xe13";
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Cookie", token);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            bufferedReader.close();
            connection.disconnect();

            result = new JsonObject(sb.toString());
            logger.log(Level.INFO, url + ": " + result);
            return getRequest().createResponse(result);
        } catch (Exception e) {
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response requestSdnApi() {
        JsonObject result = null;
        try {
            Data param = getData();
            String method = param.get("METHOD", "GET");
            String path = param.get("PATH");
            String parameter = param.get("PARAM", "");
            String body = param.get("BODY");

            String strUrl = "http://" + ConfigProperties.get().getSdnApiHost() + ":" + ConfigProperties.get().getSdnApiPort() + "/" + path + parameter;

            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", sdnTokenService.getAccessToken());
            
            if (body != null) {
                connection.setDoOutput(true); // OutputStream을 사용해서 post body 데이터 전송
                try (OutputStream os = connection.getOutputStream()) {
                    byte request_data[] = body.getBytes("utf-8");
                    os.write(request_data);
                    os.close();
                }
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            bufferedReader.close();
            connection.disconnect();

            result = new JsonObject(sb.toString());
            return getRequest().createResponse(result);
        } catch (Exception e) {
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    };

    private Response resetTicket() {
        try {
            EventProxyServer.emitRestart();
            Data data = new Data();
            data.set(Constants.JSON_RESULT_NAME, true);
            return getRequest().createResponse(Utils.createJsonObject(data));
        } catch (NullPointerException e) {
            rollback();
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response getSavedChartData() {
        return getRequest().createResponse((JsonObject)CronTriggerRunner.defaultChartData);
    }

}
