package com.example.discordia.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.discordia.model.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<UserModel, Long>{

    UserModel findByUsername (String username);
    Optional<UserModel> findByUserId(UUID userId);
    UserModel findByEmailAddress(String emailAddress);

    @NullMarked
    UserModel save(UserModel userModel);

    @Query(
            "SELECT user FROM UserModel user WHERE " +
            "(user.username LIKE CONCAT(:searchTerm, '%') OR " +
            "user.displayName LIKE CONCAT(:searchTerm, '%')) " +
            "AND user.userId != :userId "
    )
    List<UserModel> findUsers(String searchTerm, UUID userId);


}
