package com.github.drxaos.livetable.config.websocket;

import com.github.drxaos.livetable.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class SubscribeInterceptor implements ChannelInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            if (!authService.auth(headerAccessor)) {
                message = null;
            }
        }
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            if (ex == null && sent) {
                var response = authService.afterAuth(headerAccessor);
                if (response != null) {
                    try {
                        log.info("auth complete " + response);
                        Thread.sleep(50); // race condition?
                        new SimpMessagingTemplate(channel).convertAndSend(headerAccessor.getDestination(), new byte[0], response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
