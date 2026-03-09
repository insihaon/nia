package com.nia.korenrca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ibatis.session.SqlSession;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.service.util.SQLUtils;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.DataSourceType;
import com.nia.korenrca.shared.Request;
import com.nia.korenrca.shared.Response;
import com.nia.korenrca.system.DesktopSystem;


public abstract class Service {

    protected final Logger logger = Logger.getLogger(Service.class.getName());

    protected SessionFactoryFactory sessionFactory;
    private SqlSession session;
    private Request request;
    private String dataSource;

    private Data data;

    public Service() {
        super();
    }

    public ArrayList<String[]> getTypeAliases() {
        return new ArrayList<String[]>();
    }

    public ArrayList<String> getMapperResources() {
        return new ArrayList<String>();
    }

    private SqlSession openSession(boolean autoCommit) throws Exception {
        return getSessionFactoryFactory().get(getDataSource()).openSession(autoCommit);
    }

    public SqlSession getSession() throws Exception {
        if (session == null) {
            session = openSession(false);
        }
        return session;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
        initSessionFactory();
    }

    public Request getRequest() {
        return request;
    }

    public void initAutoCommit(boolean autoCommit) throws Exception {
        session = openSession(autoCommit);
    }

    public boolean isLocalServer() {
        String dataSource = getDataSourceName();
        return !"_OPER".equals(dataSource);
    }

    public String getDataSourceName() {
        return "_DEV";
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;

        consoleLog(data.toString());
    }

    public String getValue(String key) {
        return data.get(key);
    }

    public String getAction() {
        return getData().getString(Constants.ACTION_NAME);
    }

    private static Map<String, SessionFactoryFactory> sessionFactoryMap = new HashMap<String, SessionFactoryFactory>();

    private SessionFactoryFactory getSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }

        String key = SessionFactoryFactory.ATTRIBUTE_NAME + this.getClass().getSimpleName();
        sessionFactory = sessionFactoryMap.get(key);

        if (sessionFactory != null) {
            return sessionFactory;
        }

        try {
            logger.info("Create SessionFactory: " + this.getClass().getSimpleName());

            ArrayList<String[]> typeAliases = new ArrayList<String[]>();
            ArrayList<String> mapperResources = new ArrayList<String>();

            typeAliases.addAll(this.getTypeAliases());
            mapperResources.addAll(this.getMapperResources());

            sessionFactory = new SessionFactoryFactory(typeAliases, mapperResources);
            sessionFactory.createSqlSessionFactory(DataSourceType.valueOf(getDataSource()));
            sessionFactoryMap.put(key, sessionFactory);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return sessionFactory;
    }

    public void initSessionFactory() {
        this.sessionFactory = getSessionFactory();
    }

    public Response doRequest() {
        return doRequest(new Request());
    }

    public Response doRequest(Request request) {
        return doRequest(getSessionFactory(), request);
    }

    public Response doRequest(SessionFactoryFactory sessionFactory, Request request) {
        try {
            this.setData(request.getData());

            this.sessionFactory = sessionFactory;
            this.request = request;

            long currentTime = System.currentTimeMillis();
            Response response = doCustomRequest(request);

            if (response != null) {
                long timeMillis = System.currentTimeMillis() - currentTime;
                consoleLog("Elapsed Time: " + (timeMillis / 1000.0f) + " seconds");
            }
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            String msg = e.getMessage() + "<br><br>";
            for (StackTraceElement element : e.getStackTrace()) {
                msg += element.toString() + "<br>";
            }
            return request.createResponse("Server와 연결 도중 오류가 발생하였습니다: " + msg);
        }
    }

    public Response runCommand() {
        if (getAction().startsWith(Constants.COMMAND.UPDATE)) {
            return updateCommand();
        } else if (getAction().startsWith(Constants.COMMAND.INSERT)) {
            return insertCommand();
        } else if (getAction().startsWith(Constants.COMMAND.DELETE)) {
            return deleteCommand();
        } else if (getAction().endsWith(Constants.COMMAND.SELECT_PAGE2_LIST)) {
            return selectPageListCommand(true);
        } else if (getAction().endsWith(Constants.COMMAND.SELECT_PAGE_LIST)) {
            return selectPageListCommand(false);
        } else if (getAction().endsWith(Constants.COMMAND.SELECT_LIST)) {
            return selectListCommand();
        } else {
            return selectOneCommand();
        }
    }

    public Response selectOneCommand() {
        try {
            HashMap<String, Object> select = selectOne(getAction(), getData());
            return getRequest().createResponse(Utils.createJsonObject(new Data(select)));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(Utils.getStackTrace(e));
        } finally {
            close();
        }
    }

    private Response selectListCommand() {
        try {
            Data data = getData();
            ArrayList<HashMap<String, Object>> selectList = selectList(getAction(), data);
            ArrayList<Data> resultList = convertResultData(selectList);
            return getRequest().createResponse(Utils.createDocument(resultList));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(Utils.getStackTrace(e));
        } finally {
            close();
        }
    }

    // private Response selectPageListCommand() {
    //     return selectListCommand();
    // }

    private Response selectPageListCommand(boolean isNew) {
        try {
            Data data = getData();

            Data dataClone = data.clone();
            dataClone.remove("OFFSET");
            dataClone.remove("LIMIT");
            dataClone.set("COUNT_ALL", true);
            HashMap<String, Object>  allList = selectOne(getAction(), dataClone, false);
            long total = (long) allList.get("total");

            ArrayList<HashMap<String, Object>> selectList = selectList(getAction(), data);
            ArrayList<Data> resultList = convertResultData(selectList);
            
            Data result = new Data();
            result.set("list", isNew ? selectList : resultList);
            result.set("total", total);
            return getRequest().createResponse(Utils.createJsonObject(result));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return getRequest().createResponse(Utils.getStackTrace(e));
        } finally {
            close();
        }
    }

    private Response updateCommand() {
        try {
            int result = update(getAction(), getData());
            commit();

            Data data = new Data();
            data.set(Constants.JSON_RESULT_NAME, result > 0 ? true : false);
            return getRequest().createResponse(Utils.createJsonObject(data));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            rollback();
            return getRequest().createResponse(Utils.getStackTrace(e));
        } finally {
            close();
        }
    }

    private Response insertCommand() {
        try {
            int result = insert(getAction(), getData());
            commit();

            Data data = new Data();
            data.set(Constants.JSON_RESULT_NAME, result > 0 ? true : false);
            return getRequest().createResponse(Utils.createJsonObject(data));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            rollback();
            return getRequest().createResponse(Utils.getStackTrace(e));
        } finally {
            close();
        }
    }

    private Response deleteCommand() {
        try {
            int result = delete(getAction(), getData());
            commit();

            Data data = new Data();
            data.set(Constants.JSON_RESULT_NAME, result > 0 ? true : false);
            return getRequest().createResponse(Utils.createJsonObject(data));
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            rollback();
            return getRequest().createResponse(Utils.getStackTrace(e));
        } finally {
            close();
        }
    }

    public void appaendSqlLog(String msg) {
        try {
            Data requestData = getData();
            Boolean bLog = requestData.get("is_intercept_sql", false);
            if(bLog || DesktopSystem.IsDevelopmentMode) {
                requestData.set(Constants.INPUT, requestData.clone());
                requestData.set(Constants.SQLLOG, requestData.get(Constants.SQLLOG, "") + msg);
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, e.getStackTrace().toString());
        }
    }

    public Object getParameter(Object data) {
        Object parameter;
        if (data instanceof Data) {
            parameter = dataToMap((Data) data);
        } else {
            parameter = data;
        }
        return parameter;
    }

    public Map<String, Object> dataToMap(Data data) {
        Map<String, Object> parameter = null;
        if (data == null) {
            parameter = new HashMap<String, Object>();
        } else {
            parameter = data.getMap();
        }

        return parameter;
    }

    public ArrayList<Data> convertResultData(ArrayList<HashMap<String, Object>> list) {
        return convertResultData(list, false);
    }

    public ArrayList<Data> convertResultData(ArrayList<HashMap<String, Object>> list, boolean blobTobase64) {
        ArrayList<Data> dataList = new ArrayList<Data>();
        for (int i = 0; i < list.size(); i++) {
            dataList.add(new Data(list.get(i), blobTobase64));
        }

        return dataList;
    }

    public HashMap<String, Object> selectOne(String command) throws Exception {
        return selectOne(command, null);
    }

    public HashMap<String, Object> selectOne(String command, Object data) throws Exception {
        return selectOne(getSession(), command, data);
    }

    public HashMap<String, Object> selectOne(String command, Object data, boolean sql) throws Exception {
        return selectOne(getSession(), command, data, sql);
    }

    public Data selectOneData(String command, Object data) throws Exception {
        HashMap<String, Object> select = selectOne(command, data);
        Data result = new Data(select);
        return result;
    }

    public HashMap<String, Object> selectFirst(String command, Object data) throws Exception {
        ArrayList<HashMap<String, Object>> list = selectList(command, data);
        if(list != null && list.size() > 0 ){
            return list.get(0);
        } else {
            return null;
        }
    }

    public Data selectFirstData(String command, Object data) throws Exception {
        HashMap<String, Object> select = selectFirst(command, data);
        Data result = new Data(select);
        return result;
    }

    public HashMap<String, Object> selectOne(SqlSession openSession, String command, Object data) throws Exception {
        return selectOne( openSession,  command,  data, true);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> selectOne(SqlSession openSession, String command, Object data, boolean sql) throws Exception {
        if (openSession == null) {
            return null;
        }

        long currentTime = System.currentTimeMillis();

        Object parameter = getParameter(data);

        String log = consoleLog(openSession, command, parameter);

        @SuppressWarnings("unchecked")
        HashMap<String, Object> result = (HashMap<String, Object>) openSession.selectOne(command, parameter);

        long timeMillis = System.currentTimeMillis() - currentTime;
        String msg = "[" + command + "] => list size: " + (result == null ? "0" : "1") + ", Elapsed Time: " + (timeMillis / 1000.0f) + " seconds";
        consoleLog(msg);

        if(sql) appaendSqlLog(log + msg);

        return result;
    }

    public ArrayList<HashMap<String, Object>> selectList(String command) throws Exception {
        return selectList(command, null);
    }

    public ArrayList<HashMap<String, Object>> selectList(String command, Object data) throws Exception {
        return selectList(getSession(), command, data);
    }

    public ArrayList<HashMap<String, Object>> selectList(SqlSession openSession, String command, Object data) throws Exception {
        if (openSession == null) {
            return null;
        }

        long currentTime = System.currentTimeMillis();

        Object parameter = getParameter(data);

        String log = consoleLog(openSession, command, parameter);

        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, Object>> selectList = (ArrayList<HashMap<String, Object>>) (Object) openSession.selectList(command, parameter);

        long timeMillis = System.currentTimeMillis() - currentTime;
        String msg = "[" + command + "] => list size: " + (selectList == null ? "null" : Integer.toString(selectList.size())) + ", Elapsed Time: " + (timeMillis / 1000.0f) + " seconds";
        consoleLog(msg);

        appaendSqlLog(log + msg);

        return selectList;
    }

    public ArrayList<Data> selectListData(String command, Object data) throws Exception {
        ArrayList<HashMap<String, Object>> selectList = selectList(command, data);
        ArrayList<Data> result = convertResultData(selectList);
        return result;
    }

    public int insert(String command, Data data) throws Exception {
        return insert(getSession(), command, data);
    }

    public int insert(SqlSession openSession, String command, Data data) throws Exception {
        if (openSession == null) {
            return -1;
        }

        Map<String, Object> parameter = dataToMap(data);
        String log = consoleLog(openSession, command, parameter);

        long currentTime = System.currentTimeMillis();
        int result = openSession.insert(command, parameter);
        if (parameter.containsKey("ID")) {
            data.set("ID", parameter.get("ID"));
        }
        long timeMillis = System.currentTimeMillis() - currentTime;
        String msg = "[ " + command + " ] => insert count: " + result + ", Elapsed Time: " + (timeMillis / 1000.0f) + " seconds";
        consoleLog(msg);

        appaendSqlLog(log + msg);

        return result;
    }

    public int update(String command, Data data) throws Exception {
        return update(getSession(), command, data);
    }

    public int update(SqlSession openSession, String command, Data data) throws Exception {
        if (openSession == null) {
            return -1;
        }
        Map<String, Object> parameter = dataToMap(data);
        String log = consoleLog(openSession, command, parameter);

        long currentTime = System.currentTimeMillis();
        int result = openSession.update(command, parameter);
        long timeMillis = System.currentTimeMillis() - currentTime;
        String msg = "[ " + command + " ] => update count: " + result + ", Elapsed Time: " + (timeMillis / 1000.0f) + " seconds";
        consoleLog(msg);

        appaendSqlLog(log + msg);

        return result;
    }

    public int delete(String command, Data data) throws Exception {
        return delete(getSession(), command, data);
    }

    public int delete(SqlSession openSession, String command, Data data) throws Exception {
        if (openSession == null) {
            return -1;
        }
        Map<String, Object> parameter = dataToMap(data);
        String log = consoleLog(openSession, command, parameter);

        long currentTime = System.currentTimeMillis();
        int result = openSession.delete(command, parameter);
        long timeMillis = System.currentTimeMillis() - currentTime;
        String msg = "[ " + command + " ] => delete count: " + result + ", Elapsed Time: " + (timeMillis / 1000.0f) + " seconds";
        consoleLog(msg);

        appaendSqlLog(log + msg);

        return result;
    }

    public void rollback() {
        if (session != null) {
            session.rollback();
        }
    }

    public void rollback(boolean force) {
        if (session != null) {
            session.rollback(force);
        }
    }

    public void commit() {
        if (session != null) {
            session.commit();
        }
    }

    public void commit(boolean force) {
        if (session != null) {
            session.commit(force);
        }
    }

    public void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    protected abstract Response doCustomRequest(Request request);

    public Response selectOne(Request request) {
        try {
            Data result = selectOneData(request.getCommand(), request.getData());
            return request.createResponse(result);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
            return request.createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    public Response selectList(Request request) {
        try {
            ArrayList<Data> result = selectListData(request.getCommand(), request.getData());
            return request.createResponse(result);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
            return request.createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    public Response insert(Request request) {
        try {
            ArrayList<Data> dataList = request.getDataList();
            if (dataList == null) {
                insert(request.getCommand(), request.getData());
            } else {
                for (Data data : dataList) {
                    insert(request.getCommand(), data);
                }
            }
            commit();
            return request.createResponse();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
            rollback();
            return request.createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    public Response update(Request request) {
        try {
            ArrayList<Data> dataList = request.getDataList();
            if (dataList == null) {
                update(request.getCommand(), request.getData());
            } else {
                for (Data data : dataList) {
                    update(request.getCommand(), data);
                }
            }
            commit();
            return request.createResponse();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
            rollback();
            return request.createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    public Response delete(Request request) {
        try {
            ArrayList<Data> dataList = request.getDataList();
            if (dataList == null) {
                delete(request.getCommand(), request.getData());
            } else {
                for (Data data : dataList) {
                    delete(request.getCommand(), data);
                }
            }
            commit();
            return request.createResponse();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
            rollback();
            return request.createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    public void updateState(Integer job_id, boolean success, Integer id, String result_message, Data addition) throws Exception {

    }

    protected SessionFactoryFactory getSessionFactoryFactory() {
        return sessionFactory;
    }

    public String consoleLog(String msg) {
        Object isLog = data == null ? null : data.get(Constants.LOG);
        if (isLog == null || !Constants.LOG_OFF.equals(isLog)) {
            getLogger().log(Level.INFO, msg);
            return msg;
        }
        return "";
    }

    public String consoleLog(String command, Object parameter) throws Exception {
        return consoleLog(getSession(), command, parameter);
    }

    public String consoleLog(SqlSession session, String command, Object parameter) {
        Object isLog = data == null ? null : data.get(Constants.LOG);
        if (isLog == null || !Constants.LOG_OFF.equals(isLog)) {
            String sqlInfo = SQLUtils.Log(session, command, parameter);
            getLogger().log(Level.INFO, sqlInfo);
            return sqlInfo;
        }
        return "";
    }

    public Logger getLogger() {
        return Logger.getLogger(getClass().getName());
    }
}
