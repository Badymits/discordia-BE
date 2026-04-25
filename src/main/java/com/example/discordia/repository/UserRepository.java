package com.example.discordia.repository;

import com.example.discordia.dto.UserDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.discordia.model.UserModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<UserModel, Long>{

    UserModel findByUsername (String username);
    Optional<UserModel> findByUserId(UUID userId);
    UserModel findByEmailAddress(String emailAddress);

    UserModel save(UserModel userModel);


}
