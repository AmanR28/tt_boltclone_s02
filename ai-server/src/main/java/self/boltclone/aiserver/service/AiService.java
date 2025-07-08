package self.boltclone.aiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import self.boltclone.aiserver.client.AiClient;
import self.boltclone.aiserver.dto.ResponseDto;
import self.boltclone.aiserver.util.AiResponseParser;

@Service
public class AiService {
    @Autowired
    @Qualifier("mockAiClient")
    private AiClient aiClient;

    public ResponseDto getAiResponse(String prompt) {

        String aiResponse = aiClient.prompt(prompt);
        return new AiResponseParser().parseAiResponse(aiResponse);
    }

}
