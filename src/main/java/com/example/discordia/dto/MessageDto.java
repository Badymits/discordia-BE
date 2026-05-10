package com.example.discordia.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectMessageDto.class, name = "direct"),
        @JsonSubTypes.Type(value = ServerMessageDto.class, name = "server")
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@Data
public class MessageDto {

    private UUID messageId;

    private String displayName;
    private String userAvatar;
    private String message;
    private String messageImgUrl;

    private Boolean isContentWithImg;
    private Boolean isReply;
    private Boolean isEdited;

    private LocalDateTime dateTimestamp;
    private ReplyMessageDto repliedTo;
}
