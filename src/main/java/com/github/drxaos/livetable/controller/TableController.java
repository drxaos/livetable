package com.github.drxaos.livetable.controller;

import com.github.drxaos.livetable.controller.model.*;
import com.github.drxaos.livetable.service.AuthService;
import com.github.drxaos.livetable.service.MessagingService;
import com.github.drxaos.livetable.service.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TableController {

    private final AuthService authService;
    private final TableService tableService;
    private final MessagingService messagingService;

    @MessageMapping("/table/chat")
    public void chat(
            @Payload Map message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        AuthService.Auth auth = authService.getAuth(headerAccessor);
        messagingService.sendToPlace(auth, message);
    }

    @MessageMapping("/table/load")
    public void load(
            SimpMessageHeaderAccessor headerAccessor
    ) {
        AuthService.Auth auth = authService.getAuth(headerAccessor);
        TableLoadResponse tableLoadResponse = tableService.load(auth);
        messagingService.sendToClient(auth, tableLoadResponse);
    }

    @MessageMapping("/table/select")
    public void select(
            @Payload Selection selection,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        AuthService.Auth auth = authService.getAuth(headerAccessor);
        selection.setUid(auth.getUid());
        auth.setSelection(selection);
        log.info("Selection " + selection);
        messagingService.sendToPlace(auth, selection);
    }

}
