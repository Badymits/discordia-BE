package com.example.discordia.jparepository;


import com.example.discordia.model.ServerChannel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface JpaServerChannelRepository extends JpaRepository<ServerChannel, UUID> {

    // something added here
//    @Query(
//        "SELECT sc FROM ServerChannel sc WHERE sc.channelId = :channelId ORDER BY sc.createdDate ASC"
//    )
    @Query("SELECT sc FROM ServerChannel sc JOIN FETCH " +
            "sc.serverCategory sCat WHERE sCat.categoryId = :categoryId")
    Optional<List<ServerChannel>> findChannelsByCategoryId(UUID categoryId);

    @Modifying
    @Transactional
    @Query("UPDATE ServerChannel sc SET sc.channelName = :channelName, " +
            "sc.channelTopic = :channelTopic " +
            "WHERE sc.channelId = :channelId")
    void updateChannel(UUID channelId, String channelName, String channelTopic);


    Optional<ServerChannel> findByChannelId (UUID channelId);
}
