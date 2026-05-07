package com.example.discordia.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.UUID;


@Getter
@Setter
@Entity
@ToString(exclude = "userPassword")
@Table(name = "users",
        indexes = {
            @Index(
                    name = "idx_username",
                    columnList = "username"
            ),
            @Index(
                    name = "idx_displayName",
                    columnList = "displayName"
            )
        }
)
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "UserID")
    private UUID userId;

    @Setter
    @Column(name = "FirstName")
    private String firstName;

    @Setter
    @Column(name = "LastName")
    private String lastName;

    @Setter
    @Column(name = "Username")
    private String username;

    @Setter
    @Getter
    @Column(name = "DisplayName")
    private String displayName;

    @Getter
    @Setter
    @Column(name = "ImgUrl")
    private String imgUrl;

    @Getter
    @Setter
    @Column(name = "UserBio")
    private String userBio;

    @Setter
    @Column(name = "Email_address", unique = true)
    private String emailAddress;

    @Setter
    @Column(name = "User_password")
    @JsonIgnore
    private String userPassword;

//    // list to keep track of all servers that the user is the owner of
//    // ...IDK why pero sige lagay natin toh pota
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ServerOwner")
//    private List<ServerModel> serverModelOwnerList = new ArrayList<>();

}
