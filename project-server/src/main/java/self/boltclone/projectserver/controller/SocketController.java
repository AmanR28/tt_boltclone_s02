package self.boltclone.projectserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import self.boltclone.projectserver.service.SocketService;

@RestController
@Slf4j
public class SocketController {
    @Autowired
    SocketService socketService;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = sha.getSessionId();
        String projectId = sha.getFirstNativeHeader("project-id");

        if (projectId != null) {
            socketService.addProjectSession(projectId, sessionId);
            log.info("SOCKET_CONNECT_EVENT || ProjectId: {} | SessionId: {}", projectId, sessionId);
        } else {
            log.error("SOCKET_CONNECT_EVENT FAIL || ProjectId is null | SessionId: {}", sessionId);
        }
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        socketService.removeProjectSession(sessionId);
        log.info("SOCKET_DISCONNECT_EVENT || SessionId: {}", sessionId);
    }
}
