package com.github.drxaos.livetable.controller;

import com.github.drxaos.livetable.controller.model.TableLoadRequest;
import com.github.drxaos.livetable.controller.model.TableLoadResponse;
import com.github.drxaos.livetable.service.AuthService;
import com.github.drxaos.livetable.service.MessagingService;
import com.github.drxaos.livetable.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

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
            @Payload TableLoadRequest tableLoadRequest,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        AuthService.Auth auth = authService.getAuth(headerAccessor);
        TableLoadResponse tableLoadResponse = tableService.load(auth.getPlace());
        messagingService.sendToClient(auth, tableLoadResponse);
    }

}
