package com.example.discordia.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@ToString(exclude = {"userPassword", "directChannel"})
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
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
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

    @Getter
    @Setter
    @CreatedDate
    @Column(name = "AccountCreatedAt")
    private LocalDateTime accountCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DirectChannelId")
    private DirectChannel directChannel; // convert to list


}
