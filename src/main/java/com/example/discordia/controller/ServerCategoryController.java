package com.example.discordia.controller;


import com.example.discordia.dto.ServerCategoryDto;
import com.example.discordia.service.ServerCategory.ServerCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class ServerCategoryController {

    private final ServerCategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseEntity<ServerCategoryDto> createCategory(
            @RequestBody ServerCategoryDto categoryDto
    ){
        ServerCategoryDto serverCategoryDto =
                categoryService.createCategory(categoryDto);

        return ResponseEntity.ok(serverCategoryDto);
    }

    public ResponseEntity<ServerCategoryDto> getServerCategories(
        @PathVariable UUID categoryId
    ){
        ServerCategoryDto dto = categoryService.getCategoryById(categoryId);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<String> updateCategory(
            @PathVariable UUID categoryId,
            @RequestBody String categoryName
    ){

        String result = categoryService.updateCategory(categoryId, categoryName);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<Integer> deleteCategory(
            @PathVariable UUID categoryId
    ){
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }


}
