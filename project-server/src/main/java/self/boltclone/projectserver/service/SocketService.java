package self.boltclone.projectserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SocketService {
    Map<String, String> mapProjectSession = new ConcurrentHashMap<>();
    Map<String, String> mapSessionProject = new ConcurrentHashMap<>();
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public void addProjectSession(String projectId, String sessionId) {
        mapProjectSession.put(projectId, sessionId);
        mapSessionProject.put(sessionId, projectId);
    }

    public void removeProjectSession(String sessionId) {
        String projectId = mapSessionProject.get(sessionId);
        mapProjectSession.remove(projectId);
        mapSessionProject.remove(sessionId);
    }

    public void sendMessageToProject(String projectId, String message) {
        String sessionId = mapProjectSession.get(projectId);

        if (sessionId != null) {
            messagingTemplate.convertAndSendToUser(sessionId, "/topic/logs", message);
            log.info("UserLog <|> ProjectId {} | SessionId {} | Message {}", projectId, sessionId, message);
        } else {
            log.error("UserLog <|>  SessionId not found | ProjectId: {} | Message: {}", projectId, message);
        }
    }
}
