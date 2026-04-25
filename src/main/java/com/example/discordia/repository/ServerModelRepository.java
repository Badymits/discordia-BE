package com.example.discordia.repository;


import com.example.discordia.model.ServerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServerModelRepository extends JpaRepository<ServerModel, UUID> {

    // the findBy "ID" refers to the name of the field declared in the Model file
    Optional<ServerModel> findByServerId(UUID serverId);

    @Query("SELECT s FROM ServerModel s JOIN s.serverMembers sm WHERE sm.user.id = :userId")
    List<ServerModel> findServersByUserId(@Param("userId") UUID userId);


    @Query("SELECT s FROM ServerModel s " +
            "JOIN s.serverMembers sm " +
            "WHERE sm.user.username = :username") // Gamitin ang username dahil String-based na tayo
    ServerModel findServersByMemberUsername(@Param("username") String username);

}
