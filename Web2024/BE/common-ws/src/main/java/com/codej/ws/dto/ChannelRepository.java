// package com.codej.ws.dto;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import javax.annotation.Resource;

// import com.codej.base.domain.common.dto.DbUser;
// import com.codej.web.provider.JwtTokenProvider;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.HashOperations;
// import org.springframework.data.redis.core.ValueOperations;
// import org.springframework.stereotype.Service;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// @Service
// public class ChannelRepository {

//     @Autowired
//     private JwtTokenProvider jwtTokenProvider;

//     // Redis CacheKeys
//     private static final String CHANNELS = "CHANNELS"; // 채널 저장
//     public static final String SUBSCRIBE_COUNT = "SUBSCRIBE_COUNT"; // 채널에 입장한 클라이언트수 저장
//     public static final String CHANNEL_NAME = "CHANNEL_NAME"; // 채널에 입장한 클라이언트의 sessionId와 채널Name을 맵핑한 정보 저장
//     public static final String USER_INFO = "USER_INFO";

//     @Resource(name = "redisTemplate")
//     private HashOperations<String, String, Channel> hashOpsChannel;
//     @Resource(name = "redisTemplate")
//     private HashOperations<String, String, List<String> > hashOpsSessionChannel;
//     @Resource(name = "redisTemplate")
//     private HashOperations<String, String, User> hashOpsSessionUser;
//     @Resource(name = "redisTemplate")
//     private ValueOperations<String, String> valueOps;

//     // 모든 채널 조회
//     public List<Channel> findAllChannel() {
//         return hashOpsChannel.values(CHANNELS);
//     }

//     // 특정 채널 조회
//     public Channel findChannelById(String id) {
//         return hashOpsChannel.get(CHANNELS, id);
//     }

//     // 채널 생성 : 서버간 채널 공유를 위해 redis hash에 저장한다.
//     public Channel createChannel(String name) {
//         Channel channel = Channel.create(name);
//         hashOpsChannel.put(CHANNELS, channel.getUuid(), channel);
//         return channel;
//     }

//     // <연결된 세션정보, User> 맵핑 정보를 저장
//     public void setSessionUser(String sessionId, User user) {
//         hashOpsSessionUser.put(USER_INFO, sessionId, user);
//     }

//     public void setSessionUser(String sessionId, String jwtToken) {
//         User user = jwtTokenProvider.getUser(jwtToken);
//         hashOpsSessionUser.put(USER_INFO, sessionId, user);
//     }

//     // 연결된 세션정보로 User 조회
//     public User getSessionUser(String sessionId) {
//         return hashOpsSessionUser.get(USER_INFO, sessionId);
//     }

//     // <연결된 세션정보, User> 맵핑 정보를 삭제
//     public void removeSessionUser(String sessionId) {
//         hashOpsSessionUser.delete(USER_INFO, sessionId);
//     }

//     // 유저가 입장한 채널ID와 유저 세션ID 맵핑 정보 저장
//     public void setSubscribeInfo(String sessionId, String channelName) {
//         List<String> channels = getSessionChannelNames(sessionId);
//         if(channels == null) {
//             channels = new ArrayList<String>();
//         }

//         if (!channels.contains(channelName)) {
//             channels.add(channelName);
//         }

//         putSubscribeInfo(sessionId, channels);
//     }

//     public void putSubscribeInfo(String sessionId, List<String> channels) {
//         hashOpsSessionChannel.put(CHANNEL_NAME, sessionId, channels);
//     }

//     // 유저 세션으로 입장해 있는 채널 ID 조회
//     public List<String> getSessionChannelNames(String sessionId) {
//         return hashOpsSessionChannel.get(CHANNEL_NAME, sessionId);
//     }

//     // 유저 세션정보와 맵핑된 채널ID 삭제
//     public void removeSubscribeInfo(String sessionId, String channelName) {
//         List<String> channels = getSessionChannelNames(sessionId);
//         channels.remove(channelName);
//         putSubscribeInfo(sessionId, channels);
//     }

//     // 유저 세션정보와 맵핑된 모든 채널ID 삭제
//     public void removeSubscribeInfo(String sessionId) {
//         hashOpsSessionChannel.delete(CHANNEL_NAME, sessionId);
//     }

//     // 채널 유저수 조회
//     public long getUserCount(String channelName) {
//         return Long.valueOf(Optional.ofNullable(valueOps.get(SUBSCRIBE_COUNT + "_" + channelName)).orElse("0"));
//     }

//     // 채널에 입장한 유저수 +1
//     public long plusUserCount(String channelName) {
//         return Optional.ofNullable(valueOps.increment(SUBSCRIBE_COUNT + "_" + channelName)).orElse(0L);
//     }

//     // 채널에 입장한 유저수 -1
//     public long minusUserCount(String channelName) {
//         return Optional.ofNullable(valueOps.decrement(SUBSCRIBE_COUNT + "_" + channelName)).filter(count -> count > 0).orElse(0L);
//     }
// }
