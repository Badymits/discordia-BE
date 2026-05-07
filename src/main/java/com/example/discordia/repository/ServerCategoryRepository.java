package com.example.discordia.repository;

import com.example.discordia.model.ServerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface ServerCategoryRepository extends JpaRepository<ServerCategory, Long> {

    ServerCategory findByCategoryId (UUID categoryId);

    @Transactional
    @Modifying
    @Query("UPDATE ServerCategory cat SET cat.categoryName = :categoryName " +
            "WHERE cat.categoryId = :categoryId")
    void updateCategory(UUID categoryId, String categoryName);

    @Transactional
    @Modifying
    @Query(
            "DELETE FROM ServerCategory cat WHERE cat.categoryId = :categoryId"
    )
    int deleteCategory(UUID categoryId);
}
