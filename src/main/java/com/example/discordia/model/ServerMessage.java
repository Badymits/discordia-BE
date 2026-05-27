package com.example.discordia.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Setter
@Getter
@ToString
@Entity
@Table(
        name = "server_message",
        indexes = @Index(
                name = "idx_messageCreated",
                columnList = "MessageCreated"
        )
)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
public class ServerMessage extends Message {

    @ManyToOne
    @JoinColumn(name = "ChannelId")
    private ServerChannel serverChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServerMessageId")
    @Getter
    private ServerMessage repliedTo;

}
