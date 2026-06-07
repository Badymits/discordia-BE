package com.example.discordia.jparepository;

import com.example.discordia.model.ServerMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface JpaServerMembersRepository extends JpaRepository<ServerMembers, UUID> {

}
