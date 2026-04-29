package com.example.discordia.repository;


import com.example.discordia.model.ServerChannel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ServerChannelRepository extends JpaRepository<ServerChannel, UUID> {

    // something added here
//    @Query(
//        "SELECT sc FROM ServerChannel sc WHERE sc.channelId = :channelId ORDER BY sc.createdDate ASC"
//    )
    @Modifying
    @Transactional
    @Query("UPDATE ServerChannel sc SET sc.channelName = :channelName, " +
            "sc.channelTopic = :channelTopic " +
            "WHERE sc.channelId = :channelId")
    void updateChannel(UUID channelId, String channelName, String channelTopic);

    @Modifying // Required for Delete/Update operations
    @Transactional
    @Query("DELETE FROM ServerChannel sc WHERE sc.channelId = :channelId")
    int deleteChannel(UUID channelId);
    Optional<ServerChannel> findByChannelId (UUID channelId);
}
