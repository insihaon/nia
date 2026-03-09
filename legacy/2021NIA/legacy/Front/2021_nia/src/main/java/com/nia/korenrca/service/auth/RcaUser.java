package com.nia.korenrca.service.auth;

import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.properties.rca.UserProperties;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RcaUser extends AbstractUser {

    @SuppressWarnings("unused")
    private RcaAuthImpl authProvider;
    private String userid;
    private Data user;
    private JsonObject principal;

    public RcaUser() {
    }

    RcaUser(String userid, Data user, RcaAuthImpl authProvider) {
        this.userid = userid;
        this.user = user;
        this.authProvider = authProvider;
    }

    @Override
    public JsonObject principal() {
        if (principal == null) {
            principal = new JsonObject(user.getMap());
        }
        return principal;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {
        if (authProvider instanceof RcaAuthImpl) {
            this.authProvider = (RcaAuthImpl) authProvider;
        } else {
            throw new IllegalArgumentException("Not a RcaAuthImpl");
        }
    }

    @Override
    protected void doIsPermitted(String arg0, Handler<AsyncResult<Boolean>> arg1) {
    }

    public Integer getLevel() {
        return user.get(UserProperties.LVL);
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return user.getString(UserProperties.NAME);
    }

    public Long getLastlogin() { return user.get(UserProperties.LAST_LOGIN); }

//    public String getDeptCd() { return user.getString(UserProperties.DEPT_CD); }

//    public String getDeptName() {
//        return user.getString(UserProperties.DEPT_NAME);
//    }

//    public String getAgentCd() {
//        return user.getString(UserProperties.AGENCY_CD);
//    }

    public String getAgencyName() { return user.getString(UserProperties.AGENCY_NAME); }

    public String getPhoneNUmber() { return user.getString(UserProperties.PHONE_NUMBER); }

    public String getEmail() { return user.getString(UserProperties.EMAIL); }

//    public String getCompanyCd() {
//        return user.getString(UserProperties.COMPANY_CD);
//    }

//    public String getCompanyName() {
//        return user.getString(UserProperties.COMPANY_NAME);
//    }


}
