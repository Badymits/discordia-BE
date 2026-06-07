package com.example.discordia.jparepository;

import com.example.discordia.model.ServerMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaServerMessagesRepository extends JpaRepository<ServerMessage, Long> {

    @Query("SELECT sm FROM ServerMessage sm JOIN FETCH " +
            "sm.serverChannel sc WHERE sc.channelId = :channelId ORDER BY sm.dateTimestamp ASC")
    List<ServerMessage> findMessagesByChannelId(@Param("channelId") UUID channelId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(" UPDATE ServerMessage sm SET sm.message = :message, sm.isEdited = true " +
            "WHERE sm.messageId = :messageId")
    int updateServerMessage(UUID messageId, String message);

    @Modifying
    @Transactional
    @Query("DELETE FROM ServerMessage sm WHERE sm.messageId = :messageId")
    void deleteServerMessage(UUID messageId);

    @Modifying
    @Transactional
    @Query("UPDATE ServerMessage m SET m.messageImgUrl = :url WHERE m.messageId = :messageId")
    void updateImgUrl(UUID messageId, String url);

    Optional<ServerMessage> findByMessageId(UUID messageId);

    @Query("SELECT sm FROM ServerMessage sm " +
            "WHERE sm.repliedTo.messageId = :messageId")
    Optional<List<ServerMessage>> findRepliedToMessagesByMessageId(UUID messageId);
}
