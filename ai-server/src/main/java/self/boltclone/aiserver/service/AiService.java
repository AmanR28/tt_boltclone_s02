package self.boltclone.aiserver.service;

import org.springframework.stereotype.Service;

@Service
public class AiService {
    public String getAiResponse(String prompt) {
        return "AI response for prompt: " + prompt;
    }

}
