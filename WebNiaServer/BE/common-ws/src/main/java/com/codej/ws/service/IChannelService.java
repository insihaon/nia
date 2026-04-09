package com.codej.ws.service;

import java.util.Set;

import com.codej.ws.dto.SessionUser;


public interface IChannelService {
    public void addSession(String sessionId, SessionUser user);
    public void removeSession(String sessionId);
    public void addSessionChannel(String sessionId, String channelName);
    public void removeSessionChannel(String sessionId);
    public void removeSessionChannel(String sessionId, String channelName);
    public SessionUser getSessionUser(String sessionId);
    public Set<String> getSessionChannel(String sessionId);
    public long getUserCount();
    public long getSessionCount();
    public long getUserCount(String channelName);
    public long plusUserCount(String channelName);
    public long minusUserCount(String channelName);

    public void addServerSession(String sessionId);
    public Set<String> getServerSession();
    public void removeServerSession(String sessionId);
}
