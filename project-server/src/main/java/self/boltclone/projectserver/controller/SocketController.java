package self.boltclone.projectserver.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@RestController
public class SocketController {

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        String user = (sha.getUser() != null ? sha.getUser().getName() : "unknown");
        System.out.println("STOMP CONNECTED: sessionId=" + sessionId + ", user=" + user);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/logs")
    public String chat() {
        System.out.println("hi");
        return "hi";
    }
}
