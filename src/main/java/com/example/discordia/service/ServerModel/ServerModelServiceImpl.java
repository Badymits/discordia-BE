package com.example.discordia.service.ServerModel;

import com.example.discordia.service.Cloudinary.CloudinaryService;
import com.example.discordia.dto.ServerCategoryDto;
import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.dto.ServerModelDto;
import com.example.discordia.model.*;
import com.example.discordia.repository.*;


import com.example.discordia.service.ServerCategory.ServerCategoryService;
import com.example.discordia.service.ServerChannel.ServerChannelService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
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

    final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String numeric = "0123456789";


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
    @Transactional // since we're calling save() method of repository
    public ServerModelDto createServer(
            ServerModelDto dto,
            MultipartFile image
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

        serverMembersList.add(serverMember);
        serverChannelsList.add(generalChannel);
        serverCategoryList.add(serverCategory);

        ServerModel serverModel = toEntity(
                dto,
                serverMembersList,
                serverChannelsList,
                serverCategoryList,
                serverOwner,
                image
        );


        setServerMemberValues(serverMember, serverOwner, serverModel);
        setChannelValues(generalChannel, serverModel, serverCategory);
        setCategoryValues(serverCategory, serverModel, serverChannelsList);


        serverModelRepository.save(serverModel);
        serverChannelRepository.save(generalChannel);
        serverMembersRepository.save(serverMember);
        serverCategoryRepository.save(serverCategory);

        // Create new list for server channels
        List<ServerChannelDto> channelDtoList = new ArrayList<>();
        List<ServerCategoryDto> categoryDtoList = new ArrayList<>();

        channelDtoList.add(toChannelDto(generalChannel, serverCategory));
        categoryDtoList.add(toCategoryDto(serverCategory, channelDtoList));

        return toDto(serverModel, categoryDtoList, serverOwner);
    }

    public String updateServer(
            UUID serverId,
            ServerModelDto dto,
            MultipartFile image
    ){
        if (serverId == null){
            throw new EntityNotFoundException("Server Id is null! Cannot update server");
        }

        ServerModel server = serverModelRepository.findByServerId(
                serverId
        ).orElseThrow(() -> new EntityNotFoundException("Server Not Found!"));

        if (!image.isEmpty()){
            String serverImgUrl = uploadServerImage(
                    server.getServerId(),
                    image
            );

            server.setServerIcon(serverImgUrl);
        }
        if (!dto.getServerName().trim().isEmpty()){
            server.setServerName(dto.getServerName());
            server.setServerDescription(dto.getServerDescription());
        }

        serverModelRepository.save(server);


        return "Success";
    }

    @Override
    public String getServerCode (UUID serverId){

        ServerModel existingServer = serverModelRepository.findByServerId(serverId)
                .orElseThrow(() -> new EntityNotFoundException("Server Not Found"));

        if (existingServer.getServerCode() != null &&
                existingServer.getCodeExpiresAt().isAfter(LocalDateTime.now())){

            return serverModelRepository
                    .findServerByServerCode(serverId, LocalDateTime.now());
        }

        String serverCode = createServerCode();

        existingServer.setServerCode(serverCode);
        existingServer.setCodeExpiresAt(
                LocalDateTime.now().plusHours(24)
        );

        serverModelRepository.save(existingServer);

        return serverCode;
    }



    private ServerModel toEntity(
            ServerModelDto dto,
            List<ServerMembers> membersList,
            List<ServerChannel> channelList,
            List<ServerCategory> categoryList,
            UserModel user,
            MultipartFile image
        ){

        ServerModel newServerModel = new ServerModel();
        String serverImgUrl;

        newServerModel.setServerChannels(channelList);
        newServerModel.setServerCategories(categoryList);
        newServerModel.setServerMembers(membersList);

        newServerModel.setServerOwner(user);
        newServerModel.setServerName(
                dto.getServerName()
        );
        newServerModel.setServerCode(
                createServerCode()
        );
        newServerModel.setCodeExpiresAt(
                LocalDateTime.now().plusHours(24)
        );

        if (image != null){
            serverImgUrl = uploadServerImage(
                    newServerModel.getServerId(),
                    image
            );

            newServerModel.setServerIcon(
                    serverImgUrl
            );
        } else {
            serverImgUrl = "";
            newServerModel.setServerIcon(serverImgUrl);
        }

        log.info("Entity details: {}", newServerModel);

        return newServerModel;
    }

    // Mapper
    private ServerModelDto toDto(
            ServerModel entity,
            List<ServerCategoryDto> categoryDto,
            UserModel user
    ){
        ServerModelDto dto = new ServerModelDto();

        log.info("dto entity: {}", entity);
        log.info("Username: {}", entity.getServerOwner());

        dto.setServerId(entity.getServerId());
        dto.setServerName(entity.getServerName());
        dto.setServerOwner(entity.getServerOwner().getUsername());

        dto.setServerMembers(entity.getServerMembers());
        dto.setServerCategories(categoryDto);

        dto.setUserId(user.getUserId());

        dto.setServerIcon(entity.getServerIcon());
        dto.setServerInviteCode(entity.getServerCode());

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

    private void setChannelValues(
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

    private void setCategoryValues(
            ServerCategory category,
            ServerModel server,
            List<ServerChannel> serverChannels
    ){
        category.setCategoryName("Text Channels");
        category.setCategoryChannels(serverChannels);
        category.setServerModel(server);

        category.setDateCreated(
                LocalDateTime.now()
        );
    }

    private void setServerMemberValues(
            ServerMembers member,
            UserModel user,
            ServerModel server
    ){
        member.setAdmin(true);
        member.setServerNickname(user.getDisplayName());
        member.setUser(user);
        member.setServerModel(server);
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
            log.error("e: ", e);
            return "";
        }
    }

    private String createServerCode(){

        int i = 0;
        Random rand = new Random();
        StringBuilder code = new StringBuilder();

        while (i < 8){
            int choice = rand.nextInt(1, 4);

            if (choice == 1){

                char character = lowerCase.charAt(
                        ThreadLocalRandom.current().nextInt(lowerCase.length()
                        )
                );
                code.append(character);

            } else if (choice == 2) {

                char character = upperCase.charAt(
                        ThreadLocalRandom.current().nextInt(upperCase.length())
                );
                code.append(character);

            } else if (choice == 3) {

                char character = numeric.charAt(
                        ThreadLocalRandom.current().nextInt(numeric.length())
                );
                code.append(character);
            }

            i++;
        }

        return code.toString();
    }
}
