package com.example.discordia.repository;

import com.example.discordia.model.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectMessagesRepository extends JpaRepository<DirectMessage, UUID> {
}
