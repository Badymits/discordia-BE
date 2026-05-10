package com.example.discordia.repository;


import com.example.discordia.dto.UserDto;
import com.example.discordia.model.DirectChannel;
import com.example.discordia.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DirectChannelRepository extends JpaRepository<DirectChannel, UUID> {


    Optional<DirectChannel> findByDirectChannelId (UUID directChannelId);


    @Query("SELECT dc FROM DirectChannel dc " +
            "WHERE :user1 MEMBER OF dc.directChannelParticipants " +
            "AND :user2 MEMBER OF dc.directChannelParticipants " +
            "AND SIZE(dc.directChannelParticipants) = 2 ")
    Optional<DirectChannel> findByDirectChannelParticipants (UserModel user1, UserModel user2);

    @Query("SELECT dc FROM DirectChannel dc " +
            "WHERE :user MEMBER OF dc.directChannelParticipants")
    List<DirectChannel> findDirectChannelsByUserId (@Param("user") UserModel user);
}
