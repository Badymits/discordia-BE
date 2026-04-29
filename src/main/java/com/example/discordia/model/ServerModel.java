package com.example.discordia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = "serverCategories")
@Table(name = "server")
public class ServerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "ServerID")
    private UUID serverId;

    @Setter
    @Column(name = "ServerName")
    private String serverName;

    @Getter
    @Setter
    @Column(name = "ServerImgUrl")
    private String serverIcon;

    @Getter
    @Setter
    @Column(name = "ServerDescription")
    private String serverDescription;

    @Setter
    @Getter
    @JoinColumn(name = "UserID")
    // find a way to transfer ownership when user leaves... or prevent owner to leave from created server
    @ManyToOne(fetch=FetchType.LAZY)
    // user the FIELD NAME and NOT THE COLUMN NAME reference
    @JsonIgnoreProperties({
            "userPassword",
            "userBio",
            "emailAddress",
            "imgUrl",
            "firstName",
            "lastName",
            "username"
    })
    private UserModel serverOwner;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "serverModel"
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<ServerChannel> serverChannels = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serverModel")
    private List<ServerMembers> serverMembers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serverModel")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    private List<ServerCategory> serverCategories = new ArrayList<>();


}
