package com.example.discordia.service.ServerModel;


import com.example.discordia.dto.ServerCategoryDto;
import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.dto.ServerModelDto;
import com.example.discordia.model.*;

import com.example.discordia.repository.*;

import com.example.discordia.service.Cloudinary.CloudinaryService;
import com.example.discordia.service.UserService.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerModelServiceImpl implements ServerModelService {

    private final ServerModelRepository serverModelRepository;
    private final ServerChannelRepository serverChannelRepository;
    private final ServerMembersRepository serverMembersRepository;
    private final ServerCategoryRepository serverCategoryRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;


    public ServerModelDto findByServerID(UUID serverId){

        ServerModel server = serverModelRepository.findByServerId(serverId)
                .orElseThrow(() -> new RuntimeException("Server Not Found"));

        UserModel user = userRepository.findByUserId(
                server.getServerOwner().getUserId()
        ).orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        log.info("User Owner??: {}", user);


        List<ServerCategoryDto> categoryDtoList = server
                .getServerCategories()
                .stream()
                .map(category -> {

                    // Filter only channels belonging to this category
                    List<ServerChannelDto> channels = category
                            .getCategoryChannels()
                            .stream()
                            // sort using Comparator for createdDate (oldest to newest)
                            .sorted(Comparator.comparing((ServerChannel c) -> {
                                if (c.getIcon().equals("text")) return 1;
                                if (c.getIcon().equals("voice")) return 2;
                                return 3;
                            }).thenComparing(ServerChannel::getDateCreated))
                            .map(channel ->
                                    toChannelDto(channel, category)
                            ).toList();

                        return toCategoryDto(category, channels);
                    }
                ).toList();

        return toDto(
                server,
                categoryDtoList,
                user
        );
    }


    @Override
    public List<ServerModelDto> getServersByUserId(UUID userId, String username){

        List<ServerModel> serverList =
                serverModelRepository.findServersByUserId(userId);

        UserModel user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        log.info("existing models: {}",serverList.toString());

        return serverList.stream()
                .map(serverModel -> {
                    // Convert plain channels to DTOs
                    List<ServerChannelDto> channelDtos = serverModel
                            .getServerChannels()
                            .stream()
                            .map(channel ->
                                    toChannelDto(
                                        channel,
                                        channel.getServerCategory()
                                    )
                            ).toList();

                    // Convert plain categories to DTOs
                    List<ServerCategoryDto> categoryDtos = serverModel
                            .getServerCategories()
                            .stream()
                            .map(category ->
                                    toCategoryDto(category, channelDtos)
                            )
                            .toList();

                    return toDto(
                            serverModel,
                            categoryDtos,
                            user
                    );

                }).toList();
    }

    @Override
    public ServerModelDto createServer(
            ServerModelDto dto
    ){

        ServerModel newServerModel = new ServerModel();
        ServerMembers serverMember = new ServerMembers();
        ServerChannel generalChannel = new ServerChannel();
        ServerCategory serverCategory = new ServerCategory();
        UserModel serverOwner =
                userRepository.findByUserId(dto.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        List<ServerMembers> serverMembersList = new ArrayList<>();
        List<ServerChannel> serverChannelsList = new ArrayList<>();
        List<ServerCategory> serverCategoryList = new ArrayList<>();

        log.info("Server Owner: {}", serverOwner.toString());

        // setting isAdmin to true only happens in create Server endpoint
        serverMember.setUser(serverOwner);
        serverMember.setServerNickname(serverOwner.getUsername());
        serverMember.setAdmin(true);
        serverMember.setServerModel(newServerModel);


        // setting values for CHANNEL model
        serverCategory.setCategoryName("Text Channels");

        SetChannelValues(
                generalChannel,
                newServerModel,
                serverCategory
        );


        serverMembersList.add(serverMember);
        serverChannelsList.add(generalChannel);
        serverCategoryList.add(serverCategory);

        // can add serverChannelsList as is since it only contains "general"
        serverCategory.setCategoryChannels(
                serverChannelsList
        );

        serverCategory.setServerModel(
                newServerModel
        );


        // Add values to new Server object
        newServerModel.setServerMembers(
                serverMembersList
        );
        newServerModel.setServerChannels(
                serverChannelsList
        );

        newServerModel.setServerCategories(
                serverCategoryList
        );

        newServerModel.setServerName(dto.getServerName());
        newServerModel.setServerOwner(
                serverOwner
        );

        serverModelRepository.save(newServerModel);
        serverChannelRepository.save(generalChannel);
        serverMembersRepository.save(serverMember);
        serverCategoryRepository.save(serverCategory);

        // Create new list for server channels
        List<ServerChannelDto> channelDtoList = new ArrayList<>();
        List<ServerCategoryDto> categoryDtoList = new ArrayList<>();

        channelDtoList.add(
                toChannelDto(generalChannel, serverCategory)
        );
        categoryDtoList.add(
                toCategoryDto(serverCategory, channelDtoList)
        );

        return toDto(
                newServerModel,
                categoryDtoList,
                serverOwner
        );
    }


    // Mapper
    private ServerModelDto toDto(
            ServerModel entity,
            List<ServerCategoryDto> categoryDto,
            UserModel user
    ){
        ServerModelDto dto = new ServerModelDto();

        dto.setServerId(entity.getServerId());
        dto.setServerName(entity.getServerName());
        dto.setServerOwner(entity.getServerOwner().getUsername());

        dto.setServerMembers(
                entity.getServerMembers()
        );
        dto.setServerCategories(
                categoryDto
        );
        dto.setUserId(
            user.getUserId()
        );

        return dto;
    }

    private ServerChannelDto toChannelDto(
            ServerChannel channel,
            ServerCategory category
    ){

        ServerChannelDto channelDto = new ServerChannelDto();

        channelDto.setChannelId(channel.getChannelId());
        channelDto.setChannelName(channel.getChannelName());
        channelDto.setCategoryId(category.getCategoryId());

        channelDto.setIcon("text");
        channelDto.setChannelType(
                Objects.equals(
                        channel.getIcon(), "text")
                        ? "text"
                        : "voice"
        );

        return channelDto;
    }

    private ServerCategoryDto toCategoryDto(
            ServerCategory category,
            List<ServerChannelDto> serverChannels
    ){
        ServerCategoryDto categoryDto = new ServerCategoryDto();

        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCategoryName(category.getCategoryName());
        categoryDto.setCategoryChannels(serverChannels);
        categoryDto.setServerId(
                category.getServerModel().getServerId()
        );

        return categoryDto;
    }

    private void SetChannelValues(
            ServerChannel channel,
            ServerModel server,
            ServerCategory category
            ){

        channel.setServerModel(server);
        channel.setChannelName("general");
        channel.setServerCategory(category);

        channel.setIcon("text");
        channel.setDateCreated(
                LocalDateTime.now()
        );
    }

    private void SetCategoryValues(

    ){

    }

    private String uploadServerImage(UUID serverId, MultipartFile file){

        String imgUrl;
        String folderName = serverId + "_" +"Img_Folder";

        if (file.isEmpty()){
            throw new EntityNotFoundException(("Server Icon cannot be null!"));
        }

        try{
            imgUrl = cloudinaryService.uploadFile(
                    file,
                    folderName
            );

            return imgUrl;
        } catch (Exception e){
            //log.info(String.valueOf(e));
            log.error("e: ", e);
            return "";
        }
    }
}
