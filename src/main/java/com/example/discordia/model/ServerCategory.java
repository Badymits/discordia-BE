package com.example.discordia.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@ToString(exclude = "serverModel")
@Setter
@Table(name = "server_category")
public class ServerCategory {

    @Id
    @Column(name = "CategoryId")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryId;

    @Getter
    @Setter
    @Column(name = "CategoryName")
    private String categoryName;

    @CreatedDate
    @Getter
    @Setter
    @Column(nullable = false, updatable = false, name = "CategoryCreatedDate")
    private LocalDateTime dateCreated;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ServerID")
    @JsonBackReference
    private ServerModel serverModel;


    @Getter
    @Setter
    @OneToMany(
            mappedBy = "serverCategory",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true // when removing one channel (categoryChannels.remove(channel)) DB will follow
    )
    private List<ServerChannel> categoryChannels = new ArrayList<>();
}
