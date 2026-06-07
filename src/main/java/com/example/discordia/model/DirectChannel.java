package com.example.discordia.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(exclude = "directChannelParticipants")
@Entity
@Table(name = "direct_channel")
@EntityListeners(AuditingEntityListener.class)
public class DirectChannel {

    @Id
    @Column(name = "DirectChannelId")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID directChannelId;

    @Getter
    @Setter
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "directChannel",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<UserModel> directChannelParticipants;

    @Getter
    @Setter
    @Column(name = "ChannelCreated")
    @CreatedDate
    private LocalDateTime channelCreated;

    @Getter
    @Setter
    @Column(name = "isGroupConvo")
    private Boolean isGroupConvo;

    // mapped by reference must be present in the Model of the List (in this case)
    @OneToMany(
            mappedBy = "directChannelModel",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<DirectMessage> chatMessages;

    @Column(name = "isChannelRead")
    private Boolean isRead;

}
