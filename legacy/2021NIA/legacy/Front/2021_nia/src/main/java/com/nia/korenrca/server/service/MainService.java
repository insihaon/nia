package com.nia.korenrca.server.service;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.exceptions.UserNotFoundException;
import com.nia.korenrca.service.auth.RcaAuth;
import com.nia.korenrca.service.auth.RcaAuthImpl;
import com.nia.korenrca.service.auth.RcaUser;
import com.nia.korenrca.service.base.BaseDbService;
import com.nia.korenrca.service.cipher.rsa.RSA;
import com.nia.korenrca.service.cipher.tea.TEA;
import com.nia.korenrca.service.util.Base64;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.*;
import com.nia.korenrca.shared.properties.CommonProperties;
import com.nia.korenrca.shared.properties.MapperProperties;
import com.nia.korenrca.shared.properties.rca.UserProperties;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class MainService extends BaseDbService {

    private static final Logger LOGGER = LogManager.getLogger();
    private RoutingContext context = null;

    public MainService() {
        super(DataSourceType.DefaultDB.toString());
    }
    public MainService(RoutingContext context) {
        super(DataSourceType.DefaultDB.toString());
        this.context = context;
    }

    @Override
    public ArrayList<String> getMapperResources() {
        ArrayList<String> list = super.getMapperResources();
        list.add(MapperProperties.MainSqlMap);
        return list;
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    protected Response doCustomRequest(Request request) {
        Response response = null;
        if (getAction() != null) {
            switch (getAction()) {
                case Constants.Main.ACTION.SERVICE_DATA:
                    response = GetConfig();
                    break;
                case Constants.Main.ACTION.INSERT_USER:
                    response = insert_user();
                    break;
                case Constants.Main.ACTION.SELECT_FIND_USER_LIST:
                    response = select_find_user();
                    break;
                case Constants.Main.ACTION.UPDATE_USER_DATA:
                    response = update_user();
                    break;
//                case "ENCRYPT_PASSWORD":
//                    response = rsa_key2();
                default:
                    response = runCommand();
                    break;
            }
        }

        return response;
    }

    private Response GetConfig() {
        try {
            JsonObject rootJsonObject = new JsonObject();

            RcaUser user = (RcaUser) getRequest().getUser();
            JsonObject result = new JsonObject(new HashMap<>());
            if (user == null) {
                result.put(Constants.PROPERTIES.KEY.USER, Utils.createJsonObject(RcaAuthImpl.createDefaultUserData()));
            } else {
                result.put(Constants.PROPERTIES.KEY.USER, user.principal().getMap());
            }
            result.put(Constants.PROPERTIES.KEY.AUTH_USE, ConfigProperties.get().getAuthUse());
            result.put(Constants.PROPERTIES.KEY.LDAP_USE, ConfigProperties.get().getLdapUse());
            result.put(Constants.PROPERTIES.KEY.PROFILE_NAME, ConfigProperties.get().getProfileName());
            result.put(Constants.PROPERTIES.KEY.PROXY_SERVER_START, ConfigProperties.get().getProxyServerStart());
            result.put(Constants.PROPERTIES.KEY.SOCKET_SERVER_START, ConfigProperties.get().getSocketServerStart());
            result.put(Constants.PROPERTIES.KEY.SOCKET_SERVER_HOST, ConfigProperties.get().getSocketServerHost());
            result.put(Constants.PROPERTIES.KEY.SOCKET_SERVER_PORT, ConfigProperties.get().getSocketServerPort());
            result.put(Constants.PROPERTIES.KEY.NIA_OPTIONS_NODE_EDITABLE, ConfigProperties.get().getOptionsNodeEditable());
            rootJsonObject.put(Constants.JSON_RESULT_NAME, result);
            return getRequest().createResponse(rootJsonObject);
        } catch (Throwable e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
    }

    private Response select_find_user() {
        Data param = getData();
        JsonObject rootJsonObject = new JsonObject();
        ArrayList<Data> list = null;

        try {
            param.set("NAME", param.getString( "NAME"));
            param.set("USER_NUMBER", param.getString( "USER_NUMBER"));
            list = selectListData(Constants.Main.SQL.SELECT_FIND_USER_LIST, param);
            commit();
            rootJsonObject.put(CommonProperties.LIST, Utils.createJsonArray(list));
        } catch (Throwable e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            return getRequest().createResponse(e.getMessage());
        } finally {
            close();
        }
        return getRequest().createResponse(rootJsonObject);
    }


    public static String encodingPassword(String password) {

        String hashedStoredPwd = null;
        if (password != null) {
            RcaAuth authProvider = RcaAuthImpl.create(Vertx.vertx(), null, null);
            hashedStoredPwd = authProvider.computeHash(password, null, -1);
        }
        return hashedStoredPwd;
    }

    public Data rsa_key() {

        Data user = new Data();
        Data d = getData();
        RoutingContext context = this.context;

        if(context == null) {
            return null; //에러처리
        }

        HttpServerRequest req = context.request();

        if (req.method() != HttpMethod.POST) {
            context.fail(405); // Must be a POST
            return d;
        } else {
            req.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");

            Session session = context.session();
            MultiMap params = req.formAttributes();

            String encData = d.get("DATA");
            if (encData != null) {
                RSA.RSAKey rsaKey = session.get(Constants.NIA.RSA_KEY);
                JsonObject encJson = new JsonObject(encData);
                String encryptKey = encJson.getString("key");

                String encryptValue = Base64.decodeString(encJson.getString("value"));

                String teaKey = null;
                try {
                    teaKey = RSA.decryptRsa(rsaKey, encryptKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* 암호화된 text를 TEA키를 이용하여 decrypt 한다. */
                TEA tea = new TEA(teaKey);
                String data = tea.decrypt(encryptValue);
                JsonObject json = new JsonObject(data);
                json.put("pw", encodingPassword(json.getString("pw")) );

                user.set("LOGIN_ID", json.getValue("id"));
                user.set("PW", json.getValue("pw"));
                user.set("NAME", json.getValue( "username"));
                user.set("LVL", 1);
                user.set("USER_NUMBER", json.getValue("usernumber"));
                user.set("EMAIL", json.getValue("email"));
                user.set("AGENCY_NAME", json.getValue("agency_name"));
            }
        }
        user.remove("context");
        return user;
    }


    public Response insert_user() {
        Data data = rsa_key();
        JsonObject json = new JsonObject();
        int count = 0;
        try {
            count = insert(Constants.Main.SQL.INSERT_USER, data);
            commit();

            json.put("RESULT", count);
            json.put("message", true);
            return getRequest().createResponse(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("message",false);
            return getRequest().createResponse(json);
        }

    }

    public Response update_user() {
        Data data = rsa_key();
        JsonObject json = new JsonObject();
        int count = 0;
        try {
            count = update(Constants.Main.SQL.UPDATE_USER_DATA, data);
            selectOne(Constants.Main.SQL.SELECT_FIND_USER_LIST, data);
            commit();

            json.put("RESULT", count);
            return getRequest().createResponse(json);
        } catch (Exception e) {
            e.printStackTrace();
            return getRequest().createResponse(e.getMessage());
        }

    }

    public Data selectUser(Data user) throws UserNotFoundException {

        try {
            HashMap<String, Object> one = null;
            one = selectOne(Constants.Main.SQL.SELECT_LOGIN, user);
            return one == null ? null : new Data(one);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserNotFoundException();
        }
    }

    public int insertUser(Data user) {
        int count = 0;
        try {
            count = insert(Constants.Main.SQL.INSERT_USER, user);
            commit();
        } catch (Exception e) {
            e.printStackTrace();
            rollback();
        }

        return count;
    }

    public int updateUser(Data user) {
        int count = 0;
        try {
            count = update(Constants.Main.SQL.UPDATE_USER, user);
            commit();
        } catch (Exception e) {
            e.printStackTrace();
            rollback();
        }

        return count;
    }

    public int UpdateLogin(String id) {
        int count = 0;
        try {
            Data data = new Data();
            data.set(UserProperties.LOGIN_ID, id);
            count = update(Constants.Main.SQL.UPDATE_LAST_LOGIN, data);
            commit();
        } catch (Throwable e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            rollback();
        } finally {
            close();
        }

        return count;
    }

}
