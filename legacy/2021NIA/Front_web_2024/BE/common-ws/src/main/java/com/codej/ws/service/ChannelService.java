package com.codej.ws.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.codej.base.dto.Session;
import com.codej.base.utils.NetUtil;
import com.codej.ws.dto.SessionUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChannelService implements IChannelService {
    // private final AppDto appDto;
    // private final RcaApiController rcaApiController;
    
    private HashMap<String, Set<String>> hashOpsSessionChannel = new HashMap<String, Set<String>>();
    private HashMap<String, SessionUser> hashOpsSessionUser = new HashMap<String, SessionUser>();
    private HashMap<String, Set<String>> hashOpsUserSession = new HashMap<String, Set<String>>();
    private HashMap<String, Long> valueOps = new HashMap<String, Long>();
    
    private static String ipAddress = "127.0.0.1";


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
        // removeAllServerSession();

        // DB에 세션 종료 기록
        rcaRequest("updateSessionAllClosedMap",
                Session.builder()
                        .host(ipAddress)
                        .build());

        // ResourceUtil.printMemory2(CommonUtil.getStackTrace("$METHOD($FILE:$LINE)"));

        log.info("onAfterStartup : userCount={}, SessionCount={}", hashOpsUserSession.size(), hashOpsSessionUser.size());
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
        hashOpsSessionChannel.clear();
        hashOpsSessionUser.clear();
        hashOpsUserSession.clear();
    }

    public void removeSession(String sessionId) {
        SessionUser user = getSessionUser(sessionId);
        if(user != null) {
            removeUserSession(user, sessionId);
        }
        removeSessionUser(sessionId);
        removeSessionChannel(sessionId);
        removeServerSession(sessionId);
        log.info("removeSession : userCount={}, SessionCount={}", hashOpsUserSession.size(), hashOpsSessionUser.size());
    }

    public void addSession(String sessionId, SessionUser user) {
        addServerSession(sessionId);
        addUserSession(user.getUid(), sessionId);
        setSessionUser(sessionId, user);
        log.info("addSession : userCount={}, SessionCount={}", hashOpsUserSession.size(), hashOpsSessionUser.size());
    }
   
    public void addServerSession(String sessionId) {
        // Set<String> sessionIds = getServerSession();
        // sessionIds.add(sessionId);
        // hashOpsServerSession.put(SERVER_SESSION, ipAddress, sessionIds);
    }
    public Set<String> getServerSession() {
        // Set<String> set = hashOpsServerSession.get(SERVER_SESSION, ipAddress);
        // if(set == null) {
        //     set = new HashSet<String>();
        // }
        // return set;
        return null;
    }

    public void removeServerSession(String sessionId) {
        // Set<String> sessionIds = getServerSession();
        // sessionIds.remove(sessionId);
        // hashOpsServerSession.put(SERVER_SESSION, ipAddress, sessionIds);
    }

    public void removeAllServerSession() {
        // Set<String> sessionIds = getServerSession();
        // for (String sessionId : sessionIds) {
        //     removeSessionUser(sessionId);
        //     removeSessionChannel(sessionId);
        // }

        // hashOpsServerSession.delete(SERVER_SESSION, ipAddress);
    }
    
    public void addUserSession(String uid, String sessionId) {
        Set<String> sessionIds = getUserSession(uid);
        if(sessionIds.add(sessionId)){
            hashOpsUserSession.put(uid, sessionIds);
        }
    }
    public Set<String> getUserSession(String uid) {
        Set<String> set = hashOpsUserSession.get(uid);
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
        hashOpsUserSession.remove(uid);
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
                hashOpsUserSession.remove(uid);
            } else {
                // log.info("removeUserSession 3-2: sessionIdsSize > 1 ===> user_session={}, uid={}",USER_SESSION, uid);
                hashOpsUserSession.put(uid, sessionIds);
            }
        }
    }

    // <연결된 세션정보, User> 맵핑 정보를 저장
    public void setSessionUser(String sessionId, SessionUser sessionUser) {
        hashOpsSessionUser.put(sessionId, sessionUser);
        rcaRequest("insertSessionMap", 
                Session.builder()
                    .host(ipAddress)
                    .user_id(sessionUser.getUid())
                    .session_id(sessionId)
                    .build());
    }

    // 연결된 세션정보로 User 조회
    public SessionUser getSessionUser(String sessionId) {
        return hashOpsSessionUser.get(sessionId);
    }

    // <연결된 세션정보, User> 맵핑 정보를 삭제
    public void removeSessionUser(String sessionId) {
        SessionUser sessionUser = getSessionUser(sessionId);
        if (sessionUser != null) {
            hashOpsSessionUser.remove(sessionId);
            rcaRequest("updateSessionClosedMap", 
                Session.builder()
                    .host(ipAddress)
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

        if (channels.add(channelName)) {
            putSessionChannel(sessionId, channels);
        }
    }

    public void putSessionChannel(String sessionId, Set<String> channels) {
        hashOpsSessionChannel.put(sessionId, channels);
    }

    @Override
    public Set<String> getSessionChannel(String sessionId) {
        Set<String> set = hashOpsSessionChannel.get(sessionId);
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
        hashOpsSessionChannel.remove(sessionId);
    }

    public long getUserCount() {
        return hashOpsUserSession.size();
    }

    public long getSessionCount() {
        return hashOpsSessionUser.size();
    }

    // 채널 유저수 조회
    public long getUserCount(String channelName) {
        long count = valueOps.getOrDefault(channelName, (long) 0);
            return count;
    }

    // 채널에 입장한 유저수 +1
    public long plusUserCount(String channelName) {
        long count =  getUserCount(channelName) +  1;
        valueOps.put(channelName, count);
        return count;
    }

    // 채널에 입장한 유저수 -1
    public long minusUserCount(String channelName) {
        long count =  getUserCount(channelName) -  1;
        valueOps.put(channelName, count);
        return count;
    }

}
