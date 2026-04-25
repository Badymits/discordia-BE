package com.example.discordia.model;


// create separate entity for user since
// they can customize their user in each server they join in

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString(exclude = "serverModel")
@Table(name = "server_members")
public class ServerMembers {

    @Id
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID memberId;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private UserModel user;

    @Column(name = "ServerNickname")
    private String serverNickname;

    private String memberBio;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @ManyToOne
    @JoinColumn(name = "ServerID")
    @JsonBackReference
    private ServerModel serverModel;


}
