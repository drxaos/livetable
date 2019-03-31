package com.github.drxaos.livetable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessagingService {

    private final AuthService authService;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendToClient(AuthService.Auth auth, Object message) {
        messagingTemplate.convertAndSend(auth.topic, message, Map.of(
                "type", message.getClass().getSimpleName()
        ));
    }

    public void sendToPlace(AuthService.Auth auth, Object message) {
        var sessions = authService.getSessionsByPlace(auth.place);
        log.debug("send message to " + sessions.size() + " clients");
        for (AuthService.Auth a : sessions) {
            sendToClient(a, message);
        }
    }

}
