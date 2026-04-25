package com.example.discordia.repository;


import com.example.discordia.model.ServerChannel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ServerChannelRepository extends JpaRepository<ServerChannel, UUID> {

    // something added here
//    @Query(
//        "SELECT sc FROM ServerChannel sc WHERE sc.channelId = :channelId ORDER BY sc.createdDate ASC"
//    )
    Optional<ServerChannel> findByChannelId (UUID channelId);
}
