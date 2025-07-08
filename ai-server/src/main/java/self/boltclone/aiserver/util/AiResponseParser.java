package self.boltclone.aiserver.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import self.boltclone.aiserver.dto.ResponseDto;

public class AiResponseParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseDto parseAiResponse(String aiResponse) {

        try {
            JsonNode root = objectMapper.readTree(aiResponse);

            String summary = root.path("summary").asText();

            JsonNode codeNode = root.path("code");
            String codeJson = objectMapper.writeValueAsString(codeNode);

            return ResponseDto.builder()
                    .summary(summary)
                    .code(codeJson)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

}
