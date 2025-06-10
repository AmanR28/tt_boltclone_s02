package self.boltClone.enums;

import lombok.Getter;

@Getter
public enum EventConstant {
    CONTAINER_CREATE("CONTAINER_CREATE"),
    CONTAINER_CREATE_SUCCESS("CONTAINER_CREATE_SUCCESS"),
    AI_PROMPT("AI_PROMPT"),
    AI_PROMPT_SUCCESS("AI_PROMPT_SUCCESS");

    private final String eventName;

    EventConstant(String eventName) {
        this.eventName = eventName;
    }

}
