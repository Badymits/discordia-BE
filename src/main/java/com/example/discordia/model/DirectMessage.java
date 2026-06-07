package com.example.discordia.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "direct_message",
        indexes = {
                @Index(name = "idx_messageCreated", columnList = "MessageCreated"),
                @Index(name = "idx_directChannelId", columnList = "DirectChannelId")
        }
    )
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
public class DirectMessage extends Message{


    @JoinColumn(name = "DirectChannelId")
    @ManyToOne
    @JsonBackReference
    private DirectChannel directChannelModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    private DirectMessage repliedTo;

    @Column(name = "RecipientUserId")
    private UUID recipientId;

}
