package com.example.discordia.repository;

import com.example.discordia.model.ServerMembers;
import com.example.discordia.model.ServerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ServerMembersRepository extends JpaRepository<ServerMembers, UUID> {

}
