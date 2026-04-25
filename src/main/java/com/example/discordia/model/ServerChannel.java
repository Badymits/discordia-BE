package com.example.discordia.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "server_channel")
public class ServerChannel {

    @Id
    @Column(name = "ChannelId")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID channelId;

    @Setter
    @Getter
    @Column(name = "ChannelName")
    private String channelName;

    @Getter
    @Setter
    private String icon;

    @CreatedDate
    @Setter
    @Getter
    @Column(nullable = false, updatable = false, name = "ChannelCreatedDate")
    private LocalDateTime dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServerID")
    @Setter
    private ServerModel serverModel;



    @OneToMany(
        mappedBy = "serverChannel",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private List<ServerMessage> serverMessages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "CategoryId")
    @Setter
    private ServerCategory serverCategory;



}
