package com.example.discordia.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
@Entity
@Table(name = "server_message")
public class ServerMessage extends Message {

    @ManyToOne
    @JoinColumn(name = "ChannelId")
    private ServerChannel serverChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServerMessageId")
    @Getter
    private ServerMessage repliedTo;

}
