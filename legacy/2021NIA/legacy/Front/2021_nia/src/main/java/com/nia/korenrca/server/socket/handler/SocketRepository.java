package com.nia.korenrca.server.socket.handler;

import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;

import java.util.Optional;

/**
 * Created by Mobidigm on 2018-08-14.
 */
public class SocketRepository {

    private SharedData data;

    public SocketRepository(SharedData data) {
        this.data = data;
    }

    public Optional<Integer> get() {
        LocalMap<String, String> counter = data.getLocalMap("key");
        return Optional.of(counter)
            .filter(map -> !map.isEmpty())
            .map(map -> Integer.valueOf(map.get("counter")));
    }

    public void update(Integer counter) {
        LocalMap<String, String> map = data.getLocalMap("key");
        map.put("counter", counter.toString());
    }
}
