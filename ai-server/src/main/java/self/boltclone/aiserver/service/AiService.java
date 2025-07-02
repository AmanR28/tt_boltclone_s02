package self.boltclone.aiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import self.boltclone.aiserver.client.AiClient;
import self.boltclone.aiserver.dto.ResponseDto;

@Service
public class AiService {
    @Autowired
    @Qualifier("mockAiClient")
    private AiClient aiClient;

    public String getAiResponse(String prompt) {
        ResponseDto response = aiClient.prompt(prompt);
        return response.getResponse();
    }

}
