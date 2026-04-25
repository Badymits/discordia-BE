package com.example.discordia.controller;


import com.example.discordia.dto.ServerCategoryDto;
import com.example.discordia.service.ServerCategory.ServerCategoryService;
import com.example.discordia.service.ServerChannel.ServerChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class ServerCategoryController {

    private final ServerChannelService channelService;
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

}
