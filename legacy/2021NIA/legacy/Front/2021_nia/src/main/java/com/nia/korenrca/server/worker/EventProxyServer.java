package com.nia.korenrca.server.worker;

import com.nia.korenrca.constants.Constants;
import com.nia.korenrca.eventbusbridge.EventBusBridge;
import com.nia.korenrca.server.service.RCAService;
import com.nia.korenrca.service.util.Utils;
import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.shared.Data;
import com.nia.korenrca.shared.properties.rca.TicketProperties;
import com.nia.korenrca.shared.vo.RcaTicketResult;
import com.nia.korenrca.shared.vo.RcaInspectorResult;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class EventProxyServer {

    private static EventProxyVerticle verticle = null;
    private static Logger LOGGER = LogManager.getLogger();
    public static boolean isProxyServerTest = ConfigProperties.get().getProxyServerTest();

    //    https://github.com/vert-x3/vertx-rabbitmq-client/blob/master/src/test/java/io/vertx/rabbitmq/RabbitMQServiceTest.java
    public static void start() {
        getVerticle();
    }

    public static EventProxyVerticle getVerticle() {
        if (verticle == null) {
            verticle = new EventProxyVerticle();

            VertxOptions options = new VertxOptions();
            options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
            options.setBlockedThreadCheckInterval(200000000);
            Vertx vertx = Vertx.vertx(options);
            vertx.deployVerticle(verticle);

            if (EventProxyServer.isProxyServerTest) {
                //  for QueueDefine Send test
                vertx.deployVerticle(new EventProxyTestVerticle(), new DeploymentOptions().setWorker(true));
            }
        }

        return verticle;
    }

    public static void emit(JsonObject json) {
        EventProxyServer.getVerticle().emit(json);
    }

    public static void emitRestart() {
        EventProxyServer.getVerticle().emitRestart();
    }
}

class EventProxyTestVerticle extends AbstractVerticle {

    private static Logger LOGGER = LogManager.getLogger();

    private enum TEST_MODE {
        INSERT, UPDATE, LINK_UPDATE, DELETE, INSPECTOR, UNKNOWN
    }
    final static TEST_MODE test_mode = TEST_MODE.UNKNOWN;

    final static String receiveExchangeName = test_mode == TEST_MODE.INSPECTOR ? QueueDefine.receiveExchangeNameInspector : QueueDefine.receiveExchangeName;
    final static String receiveQueueName = test_mode == TEST_MODE.INSPECTOR ? QueueDefine.receiveQueueNameInspector : QueueDefine.receiveQueueName;
    final static String receiveRoutingKey = test_mode == TEST_MODE.INSPECTOR ? QueueDefine.receiveRoutingKeyInspector : QueueDefine.receiveRoutingKey;

//    final static String receiveExchangeName = QueueDefine.sendExchangeName;
//    final static String receiveQueueName = QueueDefine.sendQueueName;
//    final static String receiveRoutingKey = QueueDefine.sendRoutingKey;

    final static String host = EventProxyVerticle.host;
    final static int port = EventProxyVerticle.port;
    final static String user = EventProxyVerticle.user;
    final static String password = EventProxyVerticle.password;
    private Channel sendChannel2;

    protected int generation_ticket_id = 2900;

    public EventProxyTestVerticle() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setConnectionTimeout(3 * 1000);

        try {
            Connection sendConn2 = factory.newConnection();
            sendChannel2 = sendConn2.createChannel();
            sendChannel2.exchangeDeclare(receiveExchangeName, "topic", true, false, null);
            sendChannel2.queueDeclare(receiveQueueName, true, false, false, null);
            sendChannel2.queueBind(receiveQueueName, receiveExchangeName, receiveRoutingKey, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start() throws Exception {
        super.start();

        try {

            boolean run = true;
            while (run) {
                try {

                    AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().contentType("text/plain").contentEncoding("UTF-8").build();
                    String message = null;

                    if (generation_ticket_id < 1) {
                        generation_ticket_id = 700000;
                    }

                    switch (test_mode) {
                        case INSERT: {
                            String id = Long.toString(++generation_ticket_id);
                            long now = new Date().getTime();
                            RcaTicketResult ticket = new RcaTicketResult();
                            ticket.setTicketId("27");
                            ticket.setEventType(Constants.SocketEventType.TICKET_NEW);
                            Data data = new Data();
                            data.set("ticket_id", "27");
                            /*data.set("cluster_no", "0000000");
                            data.set("status", "INIT");
                            data.set("ticket_generation_time", now);
                            data.set("ticket_update_time", now);
                            data.set("fault_time", now);
                            data.set("root_cause_sysnamea", "시스템A");
                            data.set("root_cause_sysnamez", "시스템Z");
                            data.set("root_cause_domain", "MSPP");
                            data.set("root_cause_type", "Linkcut");
                            data.set("ticket_type", "RT");
                            data.set("influencecircuit_count", 0);
                            data.set("total_alarm_count", 0);
                            data.set("total_related_alarm_count", 0);
                            data.set("ticket_fm_nonfm", "nonFM");*/
                            ticket.setProperties(data.getProperties());
                            message = Utils.toJsonString(ticket);
                            break;
                        }
                        case UPDATE: {
                            RcaTicketResult ticket = new RcaTicketResult();
                            ticket.setTicketId("543074");
                            ticket.setEventType(Constants.SocketEventType.TICKET_UPDATE);
                            Data data = new Data();
                            data.set("ticket_rca_result_code", "PTN_RT_LINK_FAIL");
                            data.set("root_cause_code", "NNI");
                            data.set("root_cause_domain", "PTN_TO_NK");
                            data.set("ticket_rca_result_dtl_code", "RT_LINK_FAIL");
                            data.set("root_cause_type", "NODE");
                            data.set("status", "FIX");
                            ticket.setProperties(data.getProperties());
                            message = Utils.toJsonString(ticket);
                            break;
                        }
                        case LINK_UPDATE: {
                            RcaTicketResult ticket = new RcaTicketResult();
                            ticket.setTicketId("409884");
                            ticket.setEventType(Constants.SocketEventType.TICKET_CABLECUT_LINK_UPDATE);
                            message = Utils.toJsonString(ticket);
                            break;
                        }
                        case DELETE: {
                            RcaTicketResult ticket = new RcaTicketResult();
                            ticket.setEventType(Constants.SocketEventType.TICKET_DELETE);
                            message = Utils.toJsonString(ticket);
                            break;
                        }
                        case INSPECTOR: {
                            RcaInspectorResult inspector = new RcaInspectorResult();
                            inspector.setEventType("INSPECTOR_UPDATE");
                            Data data = new Data();
                            data.set("clear_time", "2020-07-08 17:10:30.0");
                            data.set("inspector_seq", Long.toString(++generation_ticket_id));
                            data.set("status", "AUTO_FIN");
                         /*   data.set("alarm_cnt", "778");
                            data.set("fault_cnt", "2");
                            data.set("monitoring_cnt", "37");
                            data.set("ocaseq", "64366");
                            data.set("optcore_total_cnt", "360");*/

                            inspector.setProperties(data.getProperties());
                            message = Utils.toJsonString(inspector);

                            /*data.set("key1", "value1");
                            data.set("inspector_seq", Long.toString(++generation_ticket_id));
                            message = Utils.toJsonString(data.getMap());*/
                            break;
                        }
                        case UNKNOWN: {
                            RcaTicketResult ticket = new RcaTicketResult();
                            ticket.setEventType("RESPONSE_RESTART_DEMO");
                            Data data = new Data();
                            ticket.setProperties(data.getProperties());
                            message = Utils.toJsonString(ticket);

                            EventBusBridge eventBusBridge = EventBusBridge.getServer();
                            if(eventBusBridge != null){
                                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_UNKNOWN, message);
                            }
                            break;
                        }
                    }

                    if (message != null && test_mode != TEST_MODE.UNKNOWN) {
                        sendChannel2.basicPublish(receiveExchangeName, receiveRoutingKey, properties, message.getBytes("UTF-8"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Thread.sleep(1000 * 5);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}


interface QueueDefine {
    String receiveExchangeName = EventProxyServer.isProxyServerTest ? "TEST_MQ_Exchange1" : ConfigProperties.get().getRabbitmqKeyReceiveExchange();
    String receiveQueueName = EventProxyServer.isProxyServerTest ? "TEST_MQ_Queue1" : ConfigProperties.get().getRabbitmqKeyReceiveQueue();
    String receiveRoutingKey = EventProxyServer.isProxyServerTest ? "TEST_MQ_RoutingKey1" : ConfigProperties.get().getRabbitmqKeyReceiveRouting();

    String receiveExchangeNameInspector = EventProxyServer.isProxyServerTest ? "TEST_MQ_Exchange2" : ConfigProperties.get().getRabbitmqKeyReceiveExchangeInspector();
    String receiveQueueNameInspector = EventProxyServer.isProxyServerTest ? "TEST_MQ_Queue2" : ConfigProperties.get().getRabbitmqKeyReceiveQueueInspector();
    String receiveRoutingKeyInspector = EventProxyServer.isProxyServerTest ? "TEST_MQ_RoutingKey2" : ConfigProperties.get().getRabbitmqKeyReceiveRoutingInspector();

    String sendExchangeName = ConfigProperties.get().getRabbitmqKeySendExchange();
    String sendQueueName = ConfigProperties.get().getRabbitmqKeySendQueue();
    String sendRoutingKey = ConfigProperties.get().getRabbitmqKeySendRouting();

    String sendTicketRestartExchangeName = ConfigProperties.get().getNiaTicketReStartExchange();
    String sendTicketRestartQueueName = ConfigProperties.get().getNiaTicketReStartQueue();
    String sendTicketRestartRoutingKey = ConfigProperties.get().getNiaTicketReStartRouting();
}

class EventProxyVerticle extends AbstractVerticle {

    private static Logger LOGGER = LogManager.getLogger();

    final static String receiveExchangeName = QueueDefine.receiveExchangeName;
    final static String receiveQueueName = QueueDefine.receiveQueueName;
    final static String receiveRoutingKey = QueueDefine.receiveRoutingKey;

    final static String receiveExchangeNameInspector = QueueDefine.receiveExchangeNameInspector;
    final static String receiveQueueNameInspector = QueueDefine.receiveQueueNameInspector;
    final static String receiveRoutingKeyInspector = QueueDefine.receiveRoutingKeyInspector;

    final static String sendExchangeName = QueueDefine.sendExchangeName;
    final static String sendQueueName = QueueDefine.sendQueueName;
    final static String sendRoutingKey = QueueDefine.sendRoutingKey;

    final static String sendTicketRestartExchangeName = QueueDefine.sendTicketRestartExchangeName;
    final static String sendTicketRestartQueueName = QueueDefine.sendTicketRestartQueueName;
    final static String sendTicketRestartRoutingKey = QueueDefine.sendTicketRestartRoutingKey;

    final static String host = ConfigProperties.get().getRabbitmqHost();
    final static int port = ConfigProperties.get().getRabbitmqPort();
    final static String user = ConfigProperties.get().getRabbitmqUser();
    final static String password = ConfigProperties.get().getRabbitmqPassword();

    final static Boolean isProxyServerStart = ConfigProperties.get().getProxyServerStart();

    private ConnectionFactory factory;
    private Channel sendChannel = null;
    private Channel sendTicketRestartChannel = null;
    private AMQP.BasicProperties basicProperties = null;
    private RCAService service = new RCAService();

    private long eventSequenceOfDay = 0;
    private String eventDate = null;

    protected  <T> Channel createRabbitMQChannel(String name, String exchange, String queue, String routingKey, boolean isReceiveChannel) {
        Connection conn = null;
        try {
            LOGGER.info(Utils.format("try RabbitMQ CONNECT: host={}, port={}, user={}, password={}", host, Integer.toString(port), user, password.replaceAll(".", "*")));

            conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(exchange, "topic", true, false, null);
            channel.queueDeclare(queue, true, false, false, null);
            channel.queueBind(queue, exchange, routingKey, null);

            if(isReceiveChannel == true) {

                String address = name;
                RabbitMQOptions config = new RabbitMQOptions();
                config.setHost(host);
                config.setPort(port);
                config.setUser(user);
                config.setPassword(password);
                config.setConnectionTimeout(6000); // in milliseconds
                config.setRequestedHeartbeat(60); // in seconds
                config.setHandshakeTimeout(6000); // in milliseconds
                config.setRequestedChannelMax(5);
                config.setNetworkRecoveryInterval(500); // in milliseconds
                config.setAutomaticRecoveryEnabled(true);

                RabbitMQClient client = RabbitMQClient.create(vertx, config);
                client.start(h -> {
                    if (isProxyServerStart && h.succeeded()) {
                        // Setup the link between rabbitmq consumer and event bus address
                        client.basicConsume(queue, address, false, consumeResult -> {
                            if (consumeResult.succeeded()) {
                                LOGGER.info(String.format("RabbitMQClient(%s) consumer created Success.", new Object[]{name}));
                            } else {
                                consumeResult.cause().printStackTrace();
                                LOGGER.info(String.format("RabbitMQClient(%s) consumer created Fail. detail=%s", new Object[]{name, consumeResult}));
                            }
                        });

                    } else {
                        LOGGER.error(String.format("RabbitMQClient(%s) 시작 중 오류로 인해 중단되었습니다.", new Object[]{name}));
                    }
                });

                vertx.eventBus().consumer(address, msg -> {
                    JsonObject json = (JsonObject) msg.body();
                    long acking = json.getLong("deliveryTag");
                    client.basicAck(acking, false, asyncResult -> {
                        if (!asyncResult.succeeded()) {
                            asyncResult.cause().printStackTrace();
                        } else {
                            try {
                                JsonObject jsonBody = new JsonObject(json.getValue("body").toString());
                                LOGGER.info(String.format("[MQ] ==============> [ME] received jsonBody=%s", new Object[]{jsonBody}));

                                jsonBody.getValue("event_type");
                                if(jsonBody.getValue("ticketId") != null) {
                                    RcaTicketResult data = Utils.convertJsonToClass(jsonBody.toString(), RcaTicketResult.class);
                                    broadcastTicket(data);
                                } else {
                                    EventBusBridge eventBusBridge = EventBusBridge.getServer();
                                    if(eventBusBridge != null){
                                        eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_UNKNOWN, jsonBody);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });
            }

            return channel;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void start() throws Exception {

        super.start();

        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setConnectionTimeout(3 * 1000);

        if (isProxyServerStart) {
            createRabbitMQChannel("TicketChannel", receiveExchangeName, receiveQueueName, receiveRoutingKey, true);
            createRabbitMQChannel("InspectorChannel", receiveExchangeNameInspector, receiveQueueNameInspector, receiveRoutingKeyInspector, true);
        }

        sendChannel = createRabbitMQChannel("SendTicketChannel", sendExchangeName, sendQueueName, sendRoutingKey, false);
        sendTicketRestartChannel = createRabbitMQChannel("SendTicketRestartChannel", sendTicketRestartExchangeName, sendTicketRestartQueueName, sendTicketRestartRoutingKey, false);

    }

    public ArrayList<HashMap<String, Object>> getTicketList(RcaTicketResult data) {

        try {
            Data param = new Data();
            param.set(TicketProperties.TICKET_ID, data.getTicketId());
            ArrayList<HashMap<String, Object>> result = service.selectList(Constants.NIA.SQL.SELECT_TICKET_CUR_LIST, param);
            return result;
        } catch (Exception e) {
        } finally {
            service.close();
        }
        return null;
    }


    // for test
    public ArrayList<HashMap<String, Object>> getTestTicketList(RcaTicketResult data) {

        try {
            Data param = new Data();
            param.set(TicketProperties.TICKET_ID, data.getTicketId());
            ArrayList<HashMap<String, Object>> result = service.selectList(Constants.NIA.SQL.SELECT_TICKET_CUR_LIST, param);

            LOGGER.info(String.format("[TC_LIST TEST] Data ================= %s  %s", data.getTicketId(), data.getProperties()));
//            LOGGER.info(String.format("[TC_LIST TEST] Param ================= %s", param.toString()));
            LOGGER.info(String.format("[TC_LIST TEST] Result ================= %s  %s", data.getTicketId(), result.get(0).get("tc_list")));
            return result;
        } catch (Exception e) {
        } finally {
            service.close();
        }
        return null;
    }

    public String gennerateEventSequence() {
        String strDataTime = Utils.getCurrentTime();
        String strDate = strDataTime.substring(0, 8);

        if (!strDate.equals(eventDate)) {
            eventDate = strDate;
            eventSequenceOfDay = 0;
        }
        return String.format("%s#%06d", new Object[]{strDataTime, eventSequenceOfDay++});
    }


    // send to Web Browser
    public void broadcastTicket(RcaTicketResult received) {
        if (received == null)
            return;

        Object data = null;
        switch (received.getEventType()) {
           /* case Constants.SocketEventType.TICKET_NEW:
                if (EventProxyServer.isProxyServerTest) {
                    Data ticket = new Data();
                    ticket.set(TicketProperties.TICKET_ID.toLowerCase(), received.getTicketId());
                    ticket.setProperties(received.getProperties());

                    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                    list.add((HashMap<String, Object>) ticket.getMap());
                    data = list;
                } else {
                    data = getTicketList(received);
                }
                break;*/
            case Constants.SocketEventType.TICKET_CABLECUT_LINK_UPDATE:
                data = getTestTicketList(received);
                break;
            case Constants.SocketEventType.TICKET_NEW:
            case Constants.SocketEventType.TICKET_UPDATE:
            case Constants.SocketEventType.TICKET_DELETE:
            case Constants.SocketEventType.TICKET_MERGE: {
                Data ticket = new Data();
                ticket.set(TicketProperties.TICKET_ID.toLowerCase(), received.getTicketId());
                ticket.setProperties(received.getProperties());

                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                list.add((HashMap<String, Object>) ticket.getMap());
                data = list;
            }
            break;
            default:
                return;
        }

        if (data == null || data instanceof ArrayList && ((ArrayList) data).size() == 0) {
            LOGGER.error("Query Result Array size 0");
            return;
        }

        String strDataTime = Utils.getCurrentTime();
        String strDate = strDataTime.substring(0, 8);

        if (!strDate.equals(eventDate)) {
            eventDate = strDate;
            eventSequenceOfDay = 0;
        }

        String strSequence = gennerateEventSequence();
        String strEventType = received.getEventType();
        String strTicketId = received.getTicketId();

        try {
            JsonObject json = new JsonObject();
            json.put("event_type", strEventType);
            json.put("sequence", strSequence);
            json.put("ticket_id", strTicketId);
            json.put("data", data);

            EventBusBridge eventBusBridge = EventBusBridge.getServer();
            if(eventBusBridge != null){
                eventBusBridge.publish(Constants.EVENT.ADDR_OUT_BROADCAST_TICKET, json);
            }
            LOGGER.debug(String.format("       [ME] ==============> [CLIENT] SOCKET PUSH [%s] : EventType=%s, TICKET_ID=%s", new Object[]{strSequence, strEventType, received.getTicketId()}));
        } catch (NullPointerException | IllegalStateException e) {
            EventBusBridge.connectionFailed();
        } catch (Exception e) {
            LOGGER.error(String.format("        [ME] ==============> [CLIENT] SOCKET PUSH 실패 [%s] : EventType=%s, TICKET_ID=%s, Error=%s", new Object[]{strSequence, strEventType, received.getTicketId(), e.getMessage()}));
        }
    }


    // send to Engine
    public void emit(JsonObject json) {
        AMQP.BasicProperties properties = getBasicProperties();
        try {
            LOGGER.info(String.format("[MQ] <============== [ME] send jsonBody=%s", new Object[]{json}));
            sendChannel.basicPublish(sendExchangeName, sendRoutingKey, properties, json.toString().getBytes("UTF-8"));
            LOGGER.info(String.format("RabbitMQ Emit Success %s", new Object[]{json.toString()}));
        } catch (IOException e) {
            LOGGER.error(String.format("RabbitMQ Emit Error %s", new Object[]{json.toString()}));
        }
    }

    //send to Engine
    public void emitRestart() {
        AMQP.BasicProperties properties = getBasicProperties();
        JsonObject json = new JsonObject();
        json.put("EVENT_TYPE", "REQUEST_RESTART_DEMO");
        try {
            LOGGER.info(String.format("[MQ] <============== [ME] send jsonBody=%s", new Object[]{json}));
            sendTicketRestartChannel.basicPublish(sendTicketRestartExchangeName, sendTicketRestartRoutingKey, properties, json.toString().getBytes("UTF-8"));
            LOGGER.info(String.format("RabbitMQ Emit Restart Success"));
        } catch (IOException e) {
            LOGGER.error(String.format("RabbitMQ Emit Restart Error"));
        }
    }

    private AMQP.BasicProperties getBasicProperties() {
        if (basicProperties == null) {
            basicProperties = new AMQP.BasicProperties.Builder().contentType("text/plain").contentEncoding("UTF-8").build();
        }
        return basicProperties;
    }

    /* 참고용 코드
    private void getMessage(RabbitMQClient client) {
        client.basicGet(receiveQueueName, true, getResult -> {
            if (getResult.succeeded()) {
                JsonObject msg = getResult.result();
                if (msg != null) {
                    LOGGER.info(String.format("RabbitMQ client Got message %s", new Object[]{msg.getValue("body")}));
                }
            } else {
                getResult.cause().printStackTrace();
            }
        });
    }*/
}
