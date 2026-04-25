package com.example.discordia.repository;

import com.example.discordia.model.ServerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServerCategoryRepository extends JpaRepository<ServerCategory, Long> {

    ServerCategory findByCategoryId (UUID categoryId);
}
