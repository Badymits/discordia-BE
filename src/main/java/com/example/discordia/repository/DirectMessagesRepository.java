package com.example.discordia.repository;

import com.example.discordia.model.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DirectMessagesRepository extends JpaRepository<DirectMessage, UUID> {

    @Query("SELECT dm FROM DirectMessage dm JOIN FETCH " +
            "dm.directChannelModel dmc WHERE dmc.directChannelId = :directChannelId ORDER BY dm.dateTimestamp")
    List<DirectMessage> findMessagesByChannelId(@Param("directChannelId") UUID directChannelId);

    //
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE DirectMessage dm SET dm.message = :message, dm.isEdited = true " +
            "WHERE dm.messageId = :messageId")
    int updateDirectMessage(UUID messageId, String message);

    @Modifying
    @Transactional
    @Query("UPDATE DirectMessage m SET m.messageImgUrl = :url WHERE m.messageId = :messageId")
    void updateImgUrl(UUID messageId, String url);

    @Modifying
    @Transactional
    @Query("DELETE FROM DirectMessage dm WHERE dm.messageId = :messageId")
    void deleteDirectMessage(UUID messageId);

    Optional<DirectMessage> findByMessageId(UUID messageId);

    @Query("SELECT dm FROM DirectMessage dm " +
            "WHERE dm.repliedTo.messageId = :messageId")
    Optional<List<DirectMessage>> findRepliedToMessagesByMessageId(UUID messageId);
}
