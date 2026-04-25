package com.example.discordia.service.ServerCategory;


import com.example.discordia.dto.ServerCategoryDto;
import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.model.ServerCategory;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.model.ServerModel;
import com.example.discordia.repository.ServerCategoryRepository;
import com.example.discordia.repository.ServerModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCategoryServiceImpl implements ServerCategoryService{

    private final ServerCategoryRepository categoryRepository;
    private final ServerModelRepository serverModelRepository;

    @Override
    public ServerCategoryDto createCategory(ServerCategoryDto dto){

        ServerCategory category = new ServerCategory();
        ServerModel existingModel =
                serverModelRepository.findByServerId(dto.getServerId())
                        .orElseThrow(() -> new RuntimeException("Server Not Found"));

        if (existingModel == null){
            throw new RuntimeException("Server Not Found!");
        }

        // get server's category list
        List<ServerCategory> existingCategoryList =
                existingModel.getServerCategories();

        // create new empty list for the category's channels
        List<ServerChannel> channelList = new ArrayList<>();

        // set category object values
        category.setCategoryName(dto.getCategoryName());
        category.setServerModel(
                existingModel
        );
        category.setCategoryChannels(channelList);

        existingCategoryList.add(category);
        categoryRepository.save(category);

        return toCategoryDto(category);
    }

    public ServerCategoryDto getCategoryById(UUID categoryId){
        ServerCategory category = categoryRepository.findByCategoryId(categoryId);

        return toCategoryDto(category);
    }

    private ServerCategoryDto toCategoryDto(
            ServerCategory category
    ){
        ServerCategoryDto dto = new ServerCategoryDto();

        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setServerId(category.getServerModel().getServerId());
        dto.setCategoryChannels(
            new ArrayList<>()
        );

        return dto;
    }


}
