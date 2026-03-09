package com.nia.korenrca.shared;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.service.util.Utils;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class ConfigProperties {

    private static ConfigProperties properties;

    private String defaultPropertiesPath;
    private String path;

    public ConfigProperties() {
    }

    public static ConfigProperties get() {
        if (properties == null) {
            properties = new ConfigProperties(Utils.getPropertiesPath());
        }
        return properties;
    }

    public ConfigProperties(String path) {
        this.path = path;
        setDefaultPropertiesPath(Utils.combine(path, Constants.PROPERTIES.CONFIG_FILENAME));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDefaultPropertiesPath() {
        return defaultPropertiesPath;
    }

    public void setDefaultPropertiesPath(String defaultPropertiesPath) {
        this.defaultPropertiesPath = defaultPropertiesPath;
    }

    public int getHttpServerPort() {
        String value = getKey(Constants.PROPERTIES.KEY.HTTP_SERVER_PORT, "8080");
        return Integer.parseInt(value);
    }

    public int getHttpsServerPort() {
        String value = getKey(Constants.PROPERTIES.KEY.HTTPS_SERVER_PORT, "8081");
        return Integer.parseInt(value);
    }

    public String getSocketServerHost() {
        String value = getKey(Constants.PROPERTIES.KEY.SOCKET_SERVER_HOST, null);
        return value;
    }

    public int getSocketServerPort() {
        String value = getKey(Constants.PROPERTIES.KEY.SOCKET_SERVER_PORT, "8084");
        return Integer.parseInt(value);
    }

    public boolean getSocketServerStart() {
        String value = getKey(Constants.PROPERTIES.KEY.SOCKET_SERVER_START, "true");
        return Boolean.parseBoolean(value);
    }

    public boolean getProxyServerStart() {
        String value = getKey(Constants.PROPERTIES.KEY.PROXY_SERVER_START, "true");
        return Boolean.parseBoolean(value);
    }

    public boolean getProxyServerTest() {
        String value = getKey(Constants.PROPERTIES.KEY.PROXY_SERVER_TEST, "false");
        return Boolean.parseBoolean(value);
    }

    public String getProfileName() {
        String value = getKey(Constants.PROPERTIES.KEY.PROFILE_NAME, "dev");
        return value;
    }

    public boolean getAuthUse() {
        String value = getKey(Constants.PROPERTIES.KEY.AUTH_USE, "true");
        return Boolean.parseBoolean(value);
    }

    public boolean getLdapUse() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_USE, "true");
        return Boolean.parseBoolean(value);
    }

    public boolean getLdapSsl() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_SSL, "false");
        return Boolean.parseBoolean(value);
    }

    public String getLdapHost() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_HOST, "ldap.kt.com");
        return value;
    }

    public String getLdapPort() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_PORT, "389");
        return value;
    }

    public String getLdapBaseDn() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_BASE_DN, "OU=Employee,DC=ktldap,DC=ktad,DC=kt,DC=net");
        return value;
    }

    public String getLdapConnId() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_CONN_ID, "ldapperson");
        return value;
    }

    public String getLdapConnPw() {
        String value = getKey(Constants.PROPERTIES.KEY.LDAP_CONN_PW, "persondlswmd");
        return value;
    }

    public String getRabbitmqHost() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_HOST, "10.81.208.123");
        return value;
    }

    public int getRabbitmqPort() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_PORT, "5675");
        return Integer.parseInt(value);
    }

    public String getRabbitmqUser() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_USER, "rcaadmin");
        return value;
    }

    public String getRabbitmqPassword() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_PW, "rcaadmin12#$");
        return value;
    }

    public String getRabbitmqKeyReceiveExchange() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_RECEIVE_EXCHANGE, "nia.exchangeEngineToUiDirectly");
        return value;
    }

    public String getRabbitmqKeyReceiveQueue() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_RECEIVE_QUEUENAME, "nia.EngineToUiIndexDirectly");
        return value;
    }

    public String getRabbitmqKeyReceiveRouting() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_RECEIVE_ROUTINGKEY, "EngineToUiIndexDirectly");
        return value;
    }

    public String getRabbitmqKeyReceiveExchangeInspector() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_RECEIVE_EXCHANGE_INSPECTOR, "rca.exchangeInspectorToUIDirectly");
        return value;
    }

    public String getRabbitmqKeyReceiveQueueInspector() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_RECEIVE_QUEUENAME_INSPECTOR, "rca.InspectorToUIDirectly");
        return value;
    }

    public String getRabbitmqKeyReceiveRoutingInspector() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_RECEIVE_ROUTINGKEY_INSPECTOR, "InspectorToUIDirectly");
        return value;
    }

    public String getRabbitmqKeySendExchange() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_SEND_EXCHANGE, "nia.exchangeUiToEngineDirectly");
        return value;
    }

    public String getRabbitmqKeySendQueue() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_SEND_QUEUENAME, "nia.UiToEngineIndexDirectly");
        return value;
    }

    public String getRabbitmqKeySendRouting() {
        String value = getKey(Constants.PROPERTIES.KEY.RABBITMQ_KEY_SEND_ROUTINGKEY, "UiToEngineIndexDirectly");
        return value;
    }

    public String getNiaTicketReStartExchange() {
        String value = getKey(Constants.PROPERTIES.KEY.RESTART_KEY_SEND_EXCHANGE, "nia.exchangeNiaTicketReStartDirectly");
        return value;
    }

    public String getNiaTicketReStartQueue() {
        String value = getKey(Constants.PROPERTIES.KEY.RESTART_KEY_SEND_QUEUENAME, "nia.NiaTicketReStartIndexDirectly");
        return value;
    }

    public String getNiaTicketReStartRouting() {
        String value = getKey(Constants.PROPERTIES.KEY.RESTART_KEY_SEND_ROUTINGKEY, "nia.NiaTicketReStartIndexDirectly");
        return value;
    }

    public String getEmailHost() { return getKey(Constants.PROPERTIES.KEY.EMAIL_HOST, null); }

    public String getEmailPort() { return getKey(Constants.PROPERTIES.KEY.EMAIL_PORT, "465"); }

    public String getEmailSsl() { return getKey(Constants.PROPERTIES.KEY.EMAIL_SSL, "true"); }

    public String getEmailAuth() { return getKey(Constants.PROPERTIES.KEY.EMAIL_AUTH, "true"); }

    public String getEmailUser() { return getKey(Constants.PROPERTIES.KEY.EMAIL_USER, null); }

    public String getEmailPassword() { return getKey(Constants.PROPERTIES.KEY.EMAIL_PASSWORD, null); }

    public String getEmailSender() { return getKey(Constants.PROPERTIES.KEY.EMAIL_FROM, "noc@koren.kr"); }

    public String getSimulatorHost() { return getKey(Constants.PROPERTIES.KEY.SIMULATOR_HOST, "127.0.0.1"); }

    public String getSimulatorPort() { return getKey(Constants.PROPERTIES.KEY.SIMULATOR_PORT, "60600"); }

    public String getPingHost() { return getKey(Constants.PROPERTIES.KEY.PING_HOST, "183.107.19.101"); }

    public String getPingPort() { return getKey(Constants.PROPERTIES.KEY.PING_PORT, "9990"); }

    public String getPingUsername() { return getKey(Constants.PROPERTIES.KEY.PING_USERNAME, "codej8888"); }

    public String getPingPassword() { return getKey(Constants.PROPERTIES.KEY.PING_PASSWORD, "zhfps!"); }

    public String getNiaApiHost() { return getKey(Constants.PROPERTIES.KEY.NIA_API_HOST, "203.255.249.31"); }

    public String getNiaApiPort() { return getKey(Constants.PROPERTIES.KEY.NIA_API_PORT, "8088"); }

    public String getSdnApiHost() { return getKey(Constants.PROPERTIES.KEY.IPSDN_API_HOST, "103.22.222.7"); }

    public String getSdnApiPort() { return getKey(Constants.PROPERTIES.KEY.IPSDN_API_PORT, "8088"); }
    public String getSdnApiUser() { return getKey(Constants.PROPERTIES.KEY.IPSDN_API_USER, "codej"); }

    public String getSdnApiPw() { return getKey(Constants.PROPERTIES.KEY.IPSDN_API_PW, "!codej@"); }

    public String getKeyStorePath() {
        String value = getKey(Constants.PROPERTIES.KEY.KEYSTORE_FILE_PATH, "config/rca.jks");
        return value;
    }

    public String getKeyStorePassword() {
        String value = getKey(Constants.PROPERTIES.KEY.KEYSTORE_PW, "rca1234");
        return value;
    }
    public String getSimulatorKeyStorePath() {
        String value = getKey(Constants.PROPERTIES.KEY.KEYSTORE_SIMULATOR_FILE_PATH, "config/tl_key.p12");
        return value;
    }

    public String getSimulatorKeyStorePassword() {
        String value = getKey(Constants.PROPERTIES.KEY.KEYSTORE_SIMULATOR_PW, "topas12!@");
        return value;
    }

    public String getWebPath() {
        return getKey(Constants.PROPERTIES.KEY.WEB_PATH, "app/websrc");
    }
    public String getAccessPath() {
        return getKey(Constants.PROPERTIES.KEY.ACCESS_PATH, "file-access");
    }

    public String getDbUrl() {
        return getKey(Constants.PROPERTIES.KEY.DB_URL, "jdbc:mysql://211.252.85.145:6033/orvdb");
    }

    public String getDbDriver() {
        return getKey(Constants.PROPERTIES.KEY.DB_DRIVER, "com.mysql.jdbc.Driver");
    }

    public String getDbPoolMax() {
        return getKey(Constants.PROPERTIES.KEY.DB_POOLMAX, "1000");
    }

    public String getDbUsername() {
        return getKey(Constants.PROPERTIES.KEY.DB_USERNAME, "everz");
    }

    public String getDbPassword() {
        return getKey(Constants.PROPERTIES.KEY.DB_PW, "everz!@34");
    }

    public boolean getWebCompressionSupported() {
        return "true".equals(getKey(Constants.PROPERTIES.KEY.WEB_COMPRESSION_SUPPORTED, "true"));
    }

    public boolean getOptionsNodeEditable() {
        return "true".equals(getKey(Constants.PROPERTIES.KEY.NIA_OPTIONS_NODE_EDITABLE, "false"));
    }

    public String getKey(String key, String defaultValue) {
        String value = getKey(key);
        return value != null ? value : defaultValue;
    }

    public String getKey(String key) {
        String value = null;
        try {
            InputStream is = new FileInputStream(defaultPropertiesPath);
            InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Properties p = null;
            try {
                p = new Properties();
                p.load(bufferedReader);
                value = p.getProperty(key);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return value;
    }

    /**
     * 프로퍼티 파일에 사용자 값을 넣는다.
     */
    public void putPropertie(Map<String, String> paramMap) throws FileNotFoundException, IOException {
        // 프로퍼티 파일 경로 key
        String propertiesKey = "properties.file.path";
        Properties proper = null;
        FileOutputStream output = null;
        try {
            String comment = (String)paramMap.get("properties.comment");
            // 사용자가 프로퍼티 파일 경로를 넘기지 않을 경우 default properties로 셋팅하다.
            if (!paramMap.containsKey(propertiesKey)) {
                paramMap.put(propertiesKey, defaultPropertiesPath);
            }
            output = new FileOutputStream((String)paramMap.get(propertiesKey));
            // paramMap.remove(propertiesKey);
            proper = new Properties();
            proper.putAll(paramMap);
            proper.store(output, comment);
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("properties 파일을 찾을수 없습니다.");
        } catch (IOException ioe) {
            throw new IOException("putPropertie Exception!", ioe);
        } finally {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

    // /**
    // * @param args
    // */
    // public static void main(String[] args) throws Exception {
    // Map<String, String> paramMap = new HashMap<String, String>();
    // paramMap.put("properties.file.path", "c:\\example12.properties");
    // paramMap.put("name", "홍길동");
    // paramMap.put("age", "31");
    // paramMap.put("phone", "0111234567");
    //
    // ConfigProperties prop = new ConfigProperties();
    // prop.putPropertie(paramMap);
    //
    // prop.setDefaultPropertiesPath("c:\\rows.properties");
    //
    // System.out.println(prop.getDefaultPropertiesPath());
    // System.out.println(prop.getKey("age"));
    // System.out.println(prop.getKey("cookiedomain"));
    // System.out.println(prop.getKey("phone"));
    // System.out.println(prop.getKey("name"));
    // }
}
