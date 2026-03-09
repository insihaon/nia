package com.nia.korenrca.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {
    public static final String SERVICE_NAME = "service";
    public static final String ACTION_NAME = "action";
    public static final String JSON_DATA_NAME = "data";
    public static final String JSON_RESULT_NAME = "result";
    public static final String JSON_ERROR_NAME = "error";

    public static final String LOG = "LOG";
    public static final String LOG_OFF = "false";

    public static final String SQL = "sql";
    public static final String SQLLOG = "sqllog";
    public static final String INPUT = "input";

    public static class DATE_FORMAT {
        public static final DateFormat MINIMAL_YYYYMMDDMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    public class SocketEventType {
        public static final String TICKET_NEW = "TICKET_NEW";
        public static final String TICKET_UPDATE = "TICKET_UPDATE";
        public static final String TICKET_MERGE = "TICKET_MERGE";
        public static final String TICKET_DELETE = "TICKET_DELETE";
        public static final String TICKET_TOPOLOGY_CHANGE = "TICKET_TOPOLOGY_CHANGE";
        public static final String TICKET_CABLECUT_LINK_UPDATE = "TICKET_CABLECUT_LINK_UPDATE";

        public static final String INSPECTOR_NEW = "INSPECTOR_NEW";
        public static final String INSPECTOR_UPDATE = "INSPECTOR_UPDATE";
    }

    public class EVENT {
        public static final String EventBusUri = "/eventbus";
        public static final String EventBusRouteUri = EventBusUri + "/*";
        public static final String WebsocketUri = EventBusUri+ "/websocket";
        public static final String ADDRESS = "address";

        public static final String ADDR_OUT_BROADCAST_MESSAGE = "broadcastMessage";
        public static final String ADDR_OUT_BROADCAST_TICKET = "broadcastTicket";
        public static final String ADDR_OUT_BROADCAST_SYSLOG = "broadcastSyslog";
        public static final String ADDR_OUT_BROADCAST_HEATBEAT = "broadcastHeatbeat";
        public static final String ADDR_OUT_DEFAULT = "out";        // for TEST
        public static final String ADDR_IN_DEFAULT = "in";          // for TEST
        public static final String ADDR_IN_SESSION = "requestSession";
        public static final String ADDR_OUT_SESSION = "session";
        public static final String ADDR_OUT_BROADCAST_NOTICE = "broadcastNotice";
        public static final String ADDR_OUT_BROADCAST_CHART = "broadcastChart";
        public static final String ADDR_OUT_BROADCAST_MONITORING = "broadcastMonitoring";
        public static final String ADDR_OUT_BROADCAST_UPDATE_STATUS = "broadcastUpdateStatus";

        public static final String ADDR_OUT_BROADCAST_UNKNOWN = "broadcastUnknown";
    }

    public class COMMAND {
        public static final String SELECT = "SELECT_";
        public static final String SELECT_LIST = "_LIST";
        public static final String SELECT_PAGE2_LIST = "_PAGE2_LIST";
        public static final String SELECT_PAGE_LIST = "_PAGE_LIST";
        public static final String UPDATE = "UPDATE_";
        public static final String INSERT = "INSERT_";
        public static final String DELETE = "DELETE_";
    }

    public class MUI {
        public class SQL {
            public static final String SELECT_TEST = "SELECT_TEST";
        }

        public class ACTION {
            public static final String SELECT_TEST = "SELECT_TEST";
        }
    }

    public class Main {
        public class SQL {
            public static final String SELECT_LOGIN = "SELECT_LOGIN";
            public static final String INSERT_USER = "INSERT_USER";
            public static final String UPDATE_USER = "UPDATE_USER";
            public static final String DELETE_USER = "DELETE_USER";
            public static final String UPDATE_LAST_LOGIN = "UPDATE_LAST_LOGIN";
            public static final String SELECT_FIND_USER_LIST = "SELECT_FIND_USER_LIST";
            public static final String UPDATE_USER_DATA = "UPDATE_USER_DATA";
        }

        public class ACTION {
            public static final String SERVICE_DATA = "SERVICE_DATA";
            public static final String INSERT_USER = "INSERT_USER";
            public static final String SELECT_FIND_USER_LIST = "SELECT_FIND_USER_LIST";
            public static final String UPDATE_USER_DATA = "UPDATE_USER_DATA";
        }
    }

    public class NIA {
        public static final String RSA_KEY = "RsaKey";
        public static final String GROUP_ID = "GROUP_ID";
        public static final String AES_KEY = "orvy";

        public class SQL {
            public static final String SELECT_CURRENT_TIME = "SELECT_CURRENT_TIME";
            public static final String SELECT_RCA_ALARM = "SELECT_RCA_ALARM";
            public static final String SELECT_MASTER_ALARM = "SELECT_MASTER_ALARM";
            public static final String SELECT_TICKET_CUR_LIST = "SELECT_TICKET_CUR_LIST";
            public static final String SELECT_LAST_SYSLOG_TIME = "SELECT_LAST_SYSLOG_TIME";

            public static final String SELECT_EVENT_CACHE_LIST = "SELECT_EVENT_CACHE_LIST";
            public static final String SELECT_EVENT_CACHE_MAX_SEQUENCE = "SELECT_EVENT_CACHE_MAX_SEQUENCE";

            public static final String SELECT_FILE_SEQUENCE = "SELECT_FILE_SEQUENCE";

            public static final String INSERT_EVENT_CACHE = "INSERT_EVENT_CACHE";
            public static final String SELECT_ISSUE_LIST = "SELECT_ISSUE_LIST";
            public static final String SELECT_QNA_LIST = "SELECT_QNA_LIST";

            public static final String SELECT_TICKET_TOPOLOGY = "SELECT_TICKET_TOPOLOGY";

            public static final String SELECT_TT_COUNT_BY_EQUIPTYPE_GRAPH = "SELECT_TT_COUNT_BY_EQUIPTYPE_GRAPH";
            public static final String SELECT_GRAPH1_DATA = "SELECT_GRAPH1_DATA";
            public static final String SELECT_GRANT_MEMBER_LIST = "SELECT_GRANT_MEMBER_LIST";

            public static final String UPDATE_MONITORING_RESULT = "UPDATE_MONITORING_RESULT";
            public static final String UPDATE_MONITORING_HIST_RESULT = "UPDATE_MONITORING_HIST_RESULT";
            public static final String SELECT_MONITORING_SYSTEM_LIST = "SELECT_MONITORING_SYSTEM_LIST";

            public static final String SELECT_NIA_SYSLOG_LIST = "SELECT_NIA_SYSLOG_LIST";
        }

        public class ACTION {
            public static final String SELECT_CURRENT_TIME = "SELECT_CURRENT_TIME";
            public static final String SELECT_RCA_ALARM = "SELECT_RCA_ALARM";
            public static final String GET_TOPOLOGY = "GET_TOPOLOGY";
            public static final String REQUEST_3D_TOPLOGY = "REQUEST_3D_TOPLOGY";
            public static final String CHANGE_TICKET_STATUS = "CHANGE_TICKET_STATUS";
            public static final String CHANGE_SYSLOG_STATUS = "CHANGE_SYSLOG_STATUS";
            public static final String EXPORT_EXCEL = "EXPORT_EXCEL";
            public static final String GET_TICKET_ALL = "GET_TICKET_ALL";
            public static final String UPDATE_ISSUE = "UPDATE_ISSUE";
            public static final String UPDATE_QNA = "UPDATE_QNA";
            public static final String UPDATE_FILE = "UPDATE_FILE";
            public static final String INSERT_ISSUE_COMMENT = "INSERT_ISSUE_COMMENT";
            public static final String INSERT_QNA_COMMENT = "INSERT_QNA_COMMENT";
            public static final String UPDATE_TICKET_TOPOLOGY = "UPDATE_TICKET_TOPOLOGY";
            public static final String DELETE_TICKET_TOPOLOGY = "DELETE_TICKET_TOPOLOGY";

            public static final String GET_DEFAULT_CHART = "GET_DEFAULT_CHART";
            public static final String UPDATE_USER_GRANT_LIST = "UPDATE_USER_GRANT_LIST";
            public static final String SELECT_EMAIL = "SELECT_EMAIL";
            public static final String SEND_PING = "SEND_PING";

            public static final String REQUEST_RESTFUL = "REQUEST_RESTFUL";
            public static final String REQUEST_HTTPS_RESTFUL = "REQUEST_HTTPS_RESTFUL";
            public static final String REQUEST_POST = "REQUEST_POST";
            public static final String REQUEST_SIMULATOR = "REQUEST_SIMULATOR";

            public static final String RESET_TICKET = "RESET_TICKET";

            public static final String REQUEST_DATA_SNAPSHOT = "REQUEST_DATA_SNAPSHOT";

            public static final String REQUEST_LOGIN_NIA = "REQUEST_LOGIN_NIA";
            public static final String REQUEST_NIA_API = "REQUEST_NIA_API";
            public static final String REQUEST_IPSDN_API = "REQUEST_IPSDN_API";
        }

        public class EQUIP_TYPE {
            public static final String ROADM = "ROADM";
            public static final String FDF = "FDF";
        }
    }

    public static class PROPERTIES {
        public static final String PATH = "./";
        public static String CONFIG_FILENAME = "config/config.properties";

        public class KEY {
            public static final String HTTP_SERVER_PORT = "http.server.port";
            public static final String HTTPS_SERVER_PORT = "https.server.port";
            public static final String SOCKET_SERVER_HOST = "socket.server.host";
            public static final String SOCKET_SERVER_PORT = "socket.server.port";
            public static final String SOCKET_SERVER_START = "socket.server.start";
            public static final String PROXY_SERVER_START = "proxy.server.start";
            public static final String PROXY_SERVER_TEST = "proxy.server.test";

            public static final String USER = "user";
            public static final String PROFILE_NAME = "profile.name";
            public static final String AUTH_USE = "auth.use";
            public static final String LDAP_USE = "ldap.use";
            public static final String LDAP_SSL = "ldap.ssl";
            public static final String LDAP_HOST = "ldap.host";
            public static final String LDAP_PORT = "ldap.port";
            public static final String LDAP_BASE_DN = "ldap.baseDN";
            public static final String LDAP_CONN_ID = "ldap.connID";
            public static final String LDAP_CONN_PW = "ldap.connPW";

            public static final String RABBITMQ_HOST = "rabbitmq.host";
            public static final String RABBITMQ_PORT = "rabbitmq.port";
            public static final String RABBITMQ_USER = "rabbitmq.user";
            public static final String RABBITMQ_PW = "rabbitmq.pw";
            public static final String RABBITMQ_KEY_RECEIVE_EXCHANGE = "rabbitmq.key.receive.exchange";
            public static final String RABBITMQ_KEY_RECEIVE_QUEUENAME = "rabbitmq.key.receive.queueName";
            public static final String RABBITMQ_KEY_RECEIVE_ROUTINGKEY = "rabbitmq.key.receive.routingKey";
            public static final String RABBITMQ_KEY_RECEIVE_EXCHANGE_INSPECTOR = "inspector.key.receive.exchange";
            public static final String RABBITMQ_KEY_RECEIVE_QUEUENAME_INSPECTOR = "inspector.key.receive.queueName";
            public static final String RABBITMQ_KEY_RECEIVE_ROUTINGKEY_INSPECTOR = "inspector.key.receive.routingKey";
            public static final String RABBITMQ_KEY_SEND_EXCHANGE = "rabbitmq.key.send.exchange";
            public static final String RABBITMQ_KEY_SEND_QUEUENAME = "rabbitmq.key.send.queueName";
            public static final String RABBITMQ_KEY_SEND_ROUTINGKEY = "rabbitmq.key.send.routingKey";

            public static final String RESTART_KEY_SEND_EXCHANGE = "restart.key.send.exchange";
            public static final String RESTART_KEY_SEND_QUEUENAME = "restart.key.send.queueName";
            public static final String RESTART_KEY_SEND_ROUTINGKEY = "restart.key.send.routingKey";

            public static final String KEYSTORE_FILE_PATH = "keystore.file.path";
            public static final String KEYSTORE_PW = "keystore.pw";
            public static final String KEYSTORE_SIMULATOR_FILE_PATH = "keystore.simulator.file.path";
            public static final String KEYSTORE_SIMULATOR_PW = "keystore.simulator.pw";

            public static final String WEB_PATH = "web.path";
            public static final String ACCESS_PATH = "access.path";
            public static final String WEB_COMPRESSION_SUPPORTED = "web.compression.supported";

            public static final String DB_URL = "db.url";
            public static final String DB_DRIVER = "db.driver";
            public static final String DB_POOLMAX = "db.poolmax";
            public static final String DB_USERNAME = "db.username";
            public static final String DB_PW = "db.pw";

            public static final String EMAIL_HOST = "email.host";
            public static final String EMAIL_PORT = "email.port";
            public static final String EMAIL_SSL = "email.ssl";
            public static final String EMAIL_AUTH = "email.auth";
            public static final String EMAIL_USER = "email.user";
            public static final String EMAIL_PASSWORD = "email.password";
            public static final String EMAIL_FROM = "email.sender";

            public static final String SIMULATOR_HOST = "simulator.host";
            public static final String SIMULATOR_PORT = "simulator.port";

            public static final String PING_HOST = "ping.server";
            public static final String PING_PORT = "ping.port";
            public static final String PING_USERNAME = "ping.username";
            public static final String PING_PASSWORD = "ping.password";

            public static final String NIA_API_HOST = "nia.api.host";
            public static final String NIA_API_PORT = "nia.api.port";

            public static final String IPSDN_API_HOST = "ipsdn.api.host";
            public static final String IPSDN_API_PORT = "ipsdn.api.port";
            public static final String IPSDN_API_USER = "ipsdn.api.user";
            public static final String IPSDN_API_PW = "ipsdn.api.pw";

            public static final String NIA_OPTIONS_NODE_EDITABLE = "nia.options.node.editable";
        }
    }
}
