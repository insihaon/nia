package com.codej.ws.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.Channel;
import com.codej.base.dto.Session;
import com.codej.base.utils.NetUtil;
import com.codej.ws.dto.SessionUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnExpression("'${spring.redis.enabled:true}' == 'true'")
public class ChannelServiceRedis implements IChannelService {
    private final AppDto appDto;
    // private final RcaApiController rcaApiController;
    
    // Redis CacheKeys
    private static final String CHANNELS = "CHANNELS"; // 채널 저장
    private static final String SUBSCRIBE_COUNT = "SUBSCRIBE_COUNT"; // 채널에 입장한 클라이언트수 저장
    private static final String SESSION_CHANNEL = "SESSION_CHANNEL"; // 채널에 입장한 클라이언트의 sessionId와 채널Name을 맵핑한 정보 저장
    private static final String SESSION_USER = "SESSION_USER";
    private static final String SERVER_SESSION = "SERVER_SESSION";
    private static final String USER_SESSION = "USER_SESSION";
    private static String ipAddress = "127.0.0.1";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Channel> hashOpsChannel;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Set<String>> hashOpsSessionChannel;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, SessionUser> hashOpsSessionUser;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Set<String>> hashOpsUserSession;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Set<String>> hashOpsServerSession;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    @PostConstruct
    public void init() {
        // IP로 구분 이전 세션 모두 제거 로직은 onAfterStartup 에서 처리
        try {
            ipAddress = NetUtil.getLocalServerIp().get(0);
        } catch (Exception e) {
            // 예외 처리
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAfterStartup() {
        // redis 에 모든 데이터 초기화
        clear();

        // 비정상 종료 전 서버에 접속되었던 세션을 모두 제거한다.
        removeAllServerSession();

        // DB에 세션 종료 기록
        rcaRequest("updateSessionAllClosedMap", Session.builder().build());

        // ResourceUtil.printMemory2(CommonUtil.getStackTrace("$METHOD($FILE:$LINE)"));
    }

    private void rcaRequest(String sqlId, Session data) {
        // if ("untact".equals(appDto.getProject())) {
        //     try {
        //         rcaApiController.request("modify", sqlId, data);
        //     } catch (Exception e) {
        //         log.error("{} {}", e.getMessage(), e);
        //     }
        // }
    }

    private void clear() {
        Set<String> keys = hashOpsChannel.keys(CHANNELS);
        for (String key : keys) {
            hashOpsChannel.delete(CHANNELS, key);
        }

        keys = hashOpsSessionChannel.keys(SESSION_CHANNEL);
        for (String key : keys) {
            hashOpsSessionChannel.delete(SESSION_CHANNEL, key);
        }

        keys = hashOpsSessionUser.keys(SESSION_USER);
        for (String key : keys) {
            removeSessionUser(key);
        }

        keys = hashOpsUserSession.keys(USER_SESSION);
        for (String key : keys) {
            hashOpsUserSession.delete(USER_SESSION, key);
        }

        keys = hashOpsServerSession.keys(SERVER_SESSION);
        for (String key : keys) {
            hashOpsServerSession.delete(SERVER_SESSION, key);
        }
    }

    public void removeSession(String sessionId) {
        SessionUser user = getSessionUser(sessionId);
        if(user != null) {
            removeUserSession(user, sessionId);
        }
        removeSessionUser(sessionId);
        removeSessionChannel(sessionId);
        removeServerSession(sessionId);
        log.info("removeSession : userCount={}, SessionCount={}", hashOpsUserSession.keys(USER_SESSION).size(), hashOpsSessionUser.keys(SESSION_USER).size());
    }

    public void addSession(String sessionId, SessionUser user) {
        addServerSession(sessionId);
        addUserSession(user.getUid(), sessionId);
        setSessionUser(sessionId, user);
        log.info("addSession : userCount={}, SessionCount={}", hashOpsUserSession.keys(USER_SESSION).size(), hashOpsSessionUser.keys(SESSION_USER).size());
    }
    // public void addSession(String sessionId, SessionUser user, String channelName) {
    //     addSession(sessionId, user);
    //     addSessionChannel(sessionId, channelName);
    // }

    // 모든 채널 조회
    public List<Channel> findAllChannel() {
        return hashOpsChannel.values(CHANNELS);
    }

    // 특정 채널 조회
    public Channel findChannelById(String id) {
        return hashOpsChannel.get(CHANNELS, id);
    }

    // 채널 생성 : 서버간 채널 공유를 위해 redis hash에 저장한다.
    public Channel createChannel(String name) {
        Channel channel = Channel.create(name);
        hashOpsChannel.put(CHANNELS, channel.getUuid(), channel);
        return channel;
    }

    public void addServerSession(String sessionId) {
        Set<String> sessionIds = getServerSession();
        sessionIds.add(sessionId);
        hashOpsServerSession.put(SERVER_SESSION, ipAddress, sessionIds);
    }
    public Set<String> getServerSession() {
        Set<String> set = hashOpsServerSession.get(SERVER_SESSION, ipAddress);
        if(set == null) {
            set = new HashSet<String>();
        }
        return set;
    }

    public void removeServerSession(String sessionId) {
        Set<String> sessionIds = getServerSession();
        sessionIds.remove(sessionId);
        hashOpsServerSession.put(SERVER_SESSION, ipAddress, sessionIds);
    }

    public void removeAllServerSession() {
        Set<String> sessionIds = getServerSession();
        for (String sessionId : sessionIds) {
            removeSessionUser(sessionId);
            removeSessionChannel(sessionId);
        }

        hashOpsServerSession.delete(SERVER_SESSION, ipAddress);
    }
    
    public void addUserSession(String uid, String sessionId) {
        Set<String> sessionIds = getUserSession(uid);
        sessionIds.add(sessionId);
        hashOpsUserSession.put(USER_SESSION, uid, sessionIds);
    }
    public Set<String> getUserSession(String uid) {
        Set<String> set = hashOpsUserSession.get(USER_SESSION, uid);
        if(set == null) {
            set = new HashSet<String>();
        }
        return set;
    }

    public void removeUserSession(String uid) {
        Set<String> sessionIds = getUserSession(uid);
        for (String sessionId : sessionIds) {
            removeSessionUser(sessionId);
            removeSessionChannel(sessionId);
        }

        hashOpsUserSession.delete(USER_SESSION, uid);
    }

    public void removeUserSession(SessionUser user, String sessionId) {
        // log.info("removeUserSession 1: user={}, sessionId={}", user, sessionId);
        if(user != null) {
            String uid = user.getUid();
            // log.info("removeUserSession 2: uid={}", uid);
            Set<String> sessionIds = getUserSession(uid);
            sessionIds.remove(sessionId);

            // log.info("removeUserSession 3: sessionIdsSize={}", sessionIds.size());
            if(sessionIds.size() < 1) {
                // log.info("removeUserSession 3-1: sessionIdsSize < 1 ===> user_session={}, uid={}", USER_SESSION,uid);
                hashOpsUserSession.delete(USER_SESSION, uid);
            } else {
                // log.info("removeUserSession 3-2: sessionIdsSize > 1 ===> user_session={}, uid={}",USER_SESSION, uid);
                hashOpsUserSession.put(USER_SESSION, uid, sessionIds);
            }
        }
    }

    // <연결된 세션정보, User> 맵핑 정보를 저장
    public void setSessionUser(String sessionId, SessionUser sessionUser) {
        hashOpsSessionUser.put(SESSION_USER, sessionId, sessionUser);
        rcaRequest("insertSessionMap", 
                Session.builder()
                    .user_id(sessionUser.getUid())
                    .session_id(sessionId)
                    .build());
    }

    // 연결된 세션정보로 User 조회
    public SessionUser getSessionUser(String sessionId) {
        return hashOpsSessionUser.get(SESSION_USER, sessionId);
    }

    // <연결된 세션정보, User> 맵핑 정보를 삭제
    public void removeSessionUser(String sessionId) {
        SessionUser sessionUser = getSessionUser(sessionId);
        if (sessionUser != null) {
            hashOpsSessionUser.delete(SESSION_USER, sessionId);
            rcaRequest("updateSessionClosedMap", 
                Session.builder()
                    .user_id(sessionUser.getUid())
                    .session_id(sessionId)
                    .build());
        }
    }

    // 유저가 입장한 채널ID와 유저 세션ID 맵핑 정보 저장
    public void addSessionChannel(String sessionId, String channelName) {
        Set<String> channels = getSessionChannel(sessionId);
        if(channels == null) {
            channels = new HashSet<String>();
        }

        if (!channels.contains(channelName)) {
            channels.add(channelName);
        }

        putSessionChannel(sessionId, channels);
    }

    public void putSessionChannel(String sessionId, Set<String> channels) {
        hashOpsSessionChannel.put(SESSION_CHANNEL, sessionId, channels);
    }

    // 유저 세션으로 입장해 있는 채널 ID 조회
    public Set<String> getSessionChannel(String sessionId) {
        Set<String> set = hashOpsSessionChannel.get(SESSION_CHANNEL, sessionId);
        if(set == null) {
            set = new HashSet<String>();
        }
        return set;
    }

    // 유저 세션정보와 맵핑된 채널ID 삭제
    public void removeSessionChannel(String sessionId, String channelName) {
        Set<String> channels = getSessionChannel(sessionId);
        channels.remove(channelName);
        if(channels.size() < 1) {
            removeSessionChannel(sessionId);
        } else {
            putSessionChannel(sessionId, channels);
        }
    }

    // 유저 세션정보와 맵핑된 모든 채널ID 삭제
    public void removeSessionChannel(String sessionId) {
        hashOpsSessionChannel.delete(SESSION_CHANNEL, sessionId);
    }

    public long getUserCount() {
        return hashOpsUserSession.keys(USER_SESSION).size();
    }

    public long getSessionCount() {
        return hashOpsSessionUser.keys(SESSION_USER).size();
    }

    // 채널 유저수 조회
    public long getUserCount(String channelName) {
        try {
            long count = (Long.valueOf(String.valueOf(valueOps.get(SUBSCRIBE_COUNT + "_" + channelName)))).longValue();    
            // log.info("getUserCount : channel={}, value={}", channelName, count);
            return count;
        } catch (Exception e) {
            return 0L;
        }
    }

    // 채널에 입장한 유저수 +1
    public long plusUserCount(String channelName) {
        long count = Optional.ofNullable(valueOps.increment(SUBSCRIBE_COUNT + "_" + channelName)).orElse(0L);
        // log.info("plusUserCount : channel={}, value={}", channelName, count);
        return count;
    }

    // 채널에 입장한 유저수 -1
    public long minusUserCount(String channelName) {
        long count = Optional.ofNullable(valueOps.decrement(SUBSCRIBE_COUNT + "_" + channelName)).filter(c -> c > 0).orElse(0L);
        // log.info("minusUserCount : channel={}, value={}", channelName, count);
        return count;
    }
}
