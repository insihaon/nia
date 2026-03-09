package com.nia.korenrca.service.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.exceptions.UserNotFoundException;
import com.nia.korenrca.server.service.MainService;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.properties.rca.UserProperties;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.PRNG;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jdbc.JDBCHashStrategy;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RcaAuthImpl implements AuthProvider, RcaAuth {

    private static final Logger LOGGER = LogManager.getLogger();

    private JDBCClient client;
    private String authenticateQuery = DEFAULT_AUTHENTICATE_QUERY;
    private JDBCHashStrategy strategy;
    private MainService service;

    public static RcaAuth create(Vertx vertx, JDBCClient client, MainService service) {
        return (RcaAuth) new RcaAuthImpl(vertx, client, service);
    }

    public RcaAuthImpl(Vertx vertx, JDBCClient client, MainService service) {
        this.client = client;
        this.service = service;
        strategy = new DefaultHashStrategy(vertx);
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {

        String userid = authInfo.getString("username");
        if (userid == null) {
            resultHandler.handle(Future.failedFuture("authInfo must contain username in 'username' field"));
            return;
        }
        String password = authInfo.getString("password");
        if (password == null) {
            resultHandler.handle(Future.failedFuture("authInfo must contain password in 'password' field"));
            return;
        }

        synchronized (service) {
            try {
                if (ConfigProperties.get().getAuthUse()) {

//                    LOGGER.trace("[authenticate:76] ");

//                    if (ConfigProperties.get().getLdapUse()) {

//                        LOGGER.trace("[authenticate:80] ");
//                        Data idms = authByLdap(userid, password);

//                        LOGGER.trace(String.format("[authenticate:83] idms: %s", new Object[]{idms.toString()}));
//                        if (idms != null) {
//                            LOGGER.trace("[authenticate:85] ");
//                            Data user = updateUser(idms, password);
//                            resultHandler.handle(Future.succeededFuture(new RcaUser(userid, user, this)));
//                            LOGGER.trace("[authenticate:90] ");
//                        } else {
//                            throw new Exception("Invalid username/password");
//                        }
//                    } else {
                        authByDb(resultHandler, userid, password);
//                    }

                } else {
                    Data user = createDefaultUserData(false);
                    resultHandler.handle(Future.succeededFuture(new RcaUser(user.getString(UserProperties.LOGIN_ID), user, this)));
                }

                service.UpdateLogin(userid);

            } catch (Exception e) {
                LOGGER.error(e);
                resultHandler.handle(Future.failedFuture(e.getMessage()));

//                if (e.getMessage().indexOf("data 0004") > 0) {
//                    비밀번호 만료일이 지났을 경우에는,
//                    kate의 비밀번호 변경 페이지를 팝업으로 띄움.
//                    http://nsso.kt.com/ssokt/pwdTab.html (w=520px, h=590px)
//                } else if (e.getMessage().indexOf("data 0005") > 0) {
//                    인증 실패 처리 필요
//                }
            } finally {
                service.close();
            }
        }
    }

    private Data authByDb(Handler<AsyncResult<User>> resultHandler, String loginId, String password) throws Exception {

        Data user = null;
        Data param = new Data();
        param.set(UserProperties.LOGIN_ID, loginId);
        param.set(UserProperties.PW, password);

        HashMap<String, Object> one = service.selectOne(Constants.Main.SQL.SELECT_LOGIN, param);

        if(one == null){
            throw new Exception("Invalid username/password");
        } else {
            user = new Data(one);

            JsonArray row = new JsonArray();
            row.add((String) user.remove(UserProperties.PW.toLowerCase())); //password
            String hashedStoredPwd = strategy.getHashedStoredPwd(row);
            String hashedPassword = strategy.computeHash(password, null, -1);
            //log.error("hashedStoredPwd == hashedPassword: " + hashedStoredPwd + " == " + hashedPassword);
            if (hashedStoredPwd.equals(hashedPassword)) {
                resultHandler.handle(Future.succeededFuture(new RcaUser(loginId, user, this)));
                service.UpdateLogin(loginId);
            } else {
                throw new Exception("Invalid username/password");
            }
        }

        return user;
    }

    @Override
    public RcaAuth setHashStrategy(JDBCHashStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    <T> void executeQuery(String query, JsonArray params, Handler<AsyncResult<T>> resultHandler,
    Consumer<ResultSet> resultSetConsumer) {
        client.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection conn = res.result();
                conn.queryWithParams(query, params, queryRes -> {
                    if (queryRes.succeeded()) {
                        ResultSet rs = queryRes.result();
                        resultSetConsumer.accept(rs);
                    } else {
                        resultHandler.handle(Future.failedFuture(queryRes.cause()));
                    }
                    conn.close(closeRes -> {
                    });
                });
            } else {
                resultHandler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    @Override
    public String computeHash(String password, String salt, int version) {
        return strategy.computeHash(password, salt, version);
    }

    @Override
    public String generateSalt() {
        return strategy.generateSalt();
    }

    @SuppressWarnings("unchecked")
    @Override
    public RcaAuth setNonces(JsonArray nonces) {
        strategy.setNonces(nonces.getList());
        return this;
    }

    private class DefaultHashStrategy implements JDBCHashStrategy {

        private final PRNG random;

        @SuppressWarnings("unused")
        private List<String> nonces;

        DefaultHashStrategy(Vertx vertx) {
            random = new PRNG(vertx);
        }

        @Override
        public String generateSalt() {
            byte[] salt = new byte[32];
            random.nextBytes(salt);

            return bytesToHex(salt);
        }

        @Override
        public String computeHash(String password, String salt, int version) {
            StringBuffer r = new StringBuffer(41);
            try {
                MessageDigest sha1er = MessageDigest.getInstance("SHA1");
                byte[] bHash = sha1er.digest(sha1er.digest(password.getBytes(StandardCharsets.UTF_8)));
                r.append("*");
                r.append(bytesToHex(bHash));
                return r.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new VertxException(e);
            }
        }

        @Override
        public String getHashedStoredPwd(JsonArray row) {
            return row.getString(0);
        }

        @Override
        public String getSalt(JsonArray row) {
            return row.getString(1);
        }

        @Override
        public void setNonces(List<String> nonces) {
            this.nonces = Collections.unmodifiableList(nonces);
        }

        private final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

            private String bytesToHex(byte[] bytes) {
            char[] chars = new char[bytes.length * 2];
            for (int i = 0; i < bytes.length; i++) {
                int x = 0xFF & bytes[i];
                chars[i * 2] = HEX_CHARS[x >>> 4];
                chars[1 + i * 2] = HEX_CHARS[0x0F & x];
            }
            return new String(chars);
        }
    }

    @Override
    public RcaAuth setAuthenticationQuery(String authenticationQuery) {
        return this;
    }

    @Override
    public RcaAuth setRolesQuery(String rolesQuery) {
        return this;
    }

    @Override
    public RcaAuth setPermissionsQuery(String permissionsQuery) {
        return this;
    }

    @Override
    public RcaAuth setRolePrefix(String rolePrefix) {
        return this;
    }

    private Data updateUser(Data idms, String password) {

//        MainService service = new MainService();

        Data user = null;
        try {
//            LOGGER.trace("[updateUser:429] ");
            user = service.selectUser(idms);
            if(user == null){
                user = idms.clone();
                user.set(UserProperties.PW, encodingPassword(password));
                user.set(UserProperties.LVL, 1);
                int count = service.insertUser(user);
//                LOGGER.trace(String.format("[updateUser:457] count=%d", new Object[]{count}));
            } else {
                service.updateUser(user);
            }
//            LOGGER.trace("[updateUser:433] ");
        } catch (UserNotFoundException e) {
            LOGGER.debug(e.getMessage());
        } catch(Exception e1){
            LOGGER.debug(e1.getMessage());
        }

        return user;
    }

    private static void insertGuestUser() throws Exception {

        MainService service = new MainService();
        Data user = createDefaultUserData(true);
        service.setData(user);

        HashMap<String, Object> one = service.selectOne(Constants.Main.SQL.SELECT_LOGIN, user);

        if(one != null){
            service.delete(Constants.Main.SQL.DELETE_USER, user);
        }

        service.insert(Constants.Main.SQL.INSERT_USER, user);
        service.commit();
        service.close();
        return;
    }

    public static RcaUser defaultUser = null;

    public static RcaUser getDefaultUser(){
        if(defaultUser == null) {
            Data data = createDefaultUserData();
            defaultUser = new RcaUser(data.getString(UserProperties.LOGIN_ID), data, null);
        }
        return defaultUser;
    }

    public static Data createDefaultUserData(){
        return createDefaultUserData(false);
    }

    public static Data createDefaultUserData(boolean includePw) {
        Data user = new Data();
        user.set(UserProperties.LOGIN_ID, "niaadmin");
        user.set(UserProperties.NAME, "NIA ADMIN");
        user.set(UserProperties.LVL, 7);
        user.set(UserProperties.AGENCY_NAME, "NOC");
        user.set(UserProperties.PHONE_NUMBER, "01000000000");
        user.set(UserProperties.EMAIL,"*****@*****");
        if (includePw) {
            user.set(UserProperties.PW, encodingPassword("niaadmin12#$"));
        }
        return user;
    }

    public static String encodingPassword(String password) {

        String hashedStoredPwd = null;
        if (password != null) {
            RcaAuth authProvider = RcaAuthImpl.create(Vertx.vertx(), null, null);
            hashedStoredPwd = authProvider.computeHash(password, null, -1);
        }
        return hashedStoredPwd;
    }

    public static void main(String[] args) throws Exception {

        // <editor-fold desc="[사용자추가]">
//        insertGuestUser();
        // <!--</editor-fold desc="[사용자추가]">

        // <editor-fold desc="[LDAP 테스트]">
//        authLdapTest();
        // <!--</editor-fold desc="[LDAP 테스트]">

        // <editor-fold desc="[사용자정보업데이트 테스트]">
//        Data user = createDefaultUserData(true);
//        LOGGER.debug("query_userinfo result="  + Utils.toJsonString(idms));
//        updateUser( createDefaultUserData(true), "aaa");
        // <!--</editor-fold desc="[사용자정보업데이트 테스트]">

    }
}
