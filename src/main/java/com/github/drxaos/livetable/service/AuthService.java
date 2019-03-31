package com.github.drxaos.livetable.service;

import com.github.drxaos.livetable.controller.model.Selection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    private Map<String, List<Auth>> placeSessions = new HashMap<>();

    public void connect(StompHeaderAccessor headerAccessor) {
        log.debug("connect " + headerAccessor.getSessionId());
    }

    public boolean auth(StompHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        Auth auth = parseDestination(destination);
        log.debug("auth " + auth);

        // todo check correct place, right secret, uniq uid, no auth

        List<Auth> sessions = placeSessions.computeIfAbsent(auth.place, k -> new ArrayList<>());
        sessions.add(auth);
        headerAccessor.getSessionAttributes().put("auth", auth);
        log.debug("authenticated; clients of [" + auth.place + "]: " + sessions.size());
        return true;
    }

    public Map<String, Object> afterAuth(StompHeaderAccessor headerAccessor) {
        Auth auth = getAuth(headerAccessor);
        log.debug("afterAuth " + auth);
        return Map.of(
                "authSuccess", "1",
                "place", auth.place,
                "uid", auth.uid
        );
    }

    public void disconnect(StompHeaderAccessor headerAccessor) {
        Auth auth = getAuth(headerAccessor);
        log.debug("disconnect " + auth);

        List<Auth> sessions = placeSessions.computeIfAbsent(auth.place, k -> new ArrayList<>());
        sessions.remove(auth);
        log.debug("disconnected; clients of [" + auth.place + "]: " + sessions.size());
    }

    public List<Auth> getSessionsByPlace(String place) {
        return placeSessions.getOrDefault(place, new ArrayList<>());
    }

    public Auth getAuth(SimpMessageHeaderAccessor headerAccessor) {
        return (Auth) headerAccessor.getSessionAttributes().get("auth");
    }

    @Data
    @AllArgsConstructor
    public static class Auth {
        final String topic, place, secret, uid;
        Selection selection;
    }

    private Auth parseDestination(String topic) {
        UriTemplate template = new UriTemplate("/topic/{place}/{secret}/{uid}");
        if (!template.matches(topic)) {
            throw new IllegalArgumentException("wrong destination");
        }
        Map<String, String> params = template.match(topic);
        String place = params.get("place");
        String secret = params.get("secret");
        String uid = params.get("uid");

        return new Auth(topic, place, secret, uid,
                new Selection(uid, -1, -1, -1, -1, false));
    }
}
