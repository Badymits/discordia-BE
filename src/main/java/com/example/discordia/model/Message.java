package com.example.discordia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@MappedSuperclass
public class Message {

    @Id
    @Column(name = "MessageID")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageId;

    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "ServerIdReference")
    private UUID serverId;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private UserModel user;

    @Setter
    @Column(name = "Message", columnDefinition = "TEXT")
    private String message;

    @Setter
    @Getter
    @Column(name = "MessageImgUrl")
    private String messageImgUrl;

    @Column(name = "UnreadMessages", columnDefinition = "integer default 0")
    private Integer unreadMessages = 0;

    @Getter
    @Setter
    @Column(name = "IsContentWithImg")
    private Boolean isContentWithImg;

    @Setter
    @Column(name = "IsReply")
    private Boolean isReply;

    @Setter
    @Column(name = "isEdited")
    private Boolean isEdited;

    @Setter
    @Column(name = "isServerInvite")
    private Boolean isServerInvite;

    @Column(name = "isMessageRead")
    private Boolean isRead;

    @CreatedDate
    @Setter
    @Column(nullable = false, updatable = false, name = "MessageCreated")
    private LocalDateTime dateTimestamp;

}
