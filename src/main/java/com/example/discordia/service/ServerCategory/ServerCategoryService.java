package com.example.discordia.service.ServerCategory;

import com.example.discordia.dto.ServerCategoryDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ServerCategoryService {

    ServerCategoryDto createCategory(ServerCategoryDto categoryDto);
    ServerCategoryDto getCategoryById(UUID categoryId);

    String updateCategory(UUID categoryId, String categoryName);
    int deleteCategory(UUID categoryId);
}
