package com.example.discordia.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "direct_message")
public class DirectMessage extends Message{


    @JoinColumn(name = "DirectMessageId")
    @ManyToOne
    private DirectChannel directMessageModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    private DirectMessage repliedTo;

}
