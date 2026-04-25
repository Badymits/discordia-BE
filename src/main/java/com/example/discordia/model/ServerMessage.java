package com.example.discordia.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@ToString
@Table(name = "server_messages")
public class ServerMessage {

    @Id
    @Column(name = "MessageID")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageId;

    @Setter
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

    @CreatedDate
    @Setter
    @Column(nullable = false, updatable = false, name = "MessageCreated")
    private LocalDateTime dateTimestamp;

    @Getter
    @Setter
    @Column(name = "IsContentWithImg")
    private Boolean isContentWithImg;

    @Setter
    @Column(name = "IsReply")
    private Boolean isReply;

    @ManyToOne
    @JoinColumn(name = "ChannelId")
    @Setter
    private ServerChannel serverChannel;


}
