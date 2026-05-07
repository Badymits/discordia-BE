package com.example.discordia.service.UserService;

import com.example.discordia.service.Cloudinary.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.discordia.model.UserModel;
import com.example.discordia.dto.UserDto;
import com.example.discordia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;


    @Override
    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto findByUserId(UUID userId){

        UserModel existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        return toDto(existingUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, MultipartFile image){

        UserModel existingUser = userRepository.findByUserId(
                userDto.getUserId()
        ).orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        String userImageUrl;
        String folderName = "User_" + existingUser.getUserId() + "_" + "Avatar_Img_Folder";

        if (image != null){
            try{
                // upload function in cloudinary config returns a URL string;
                userImageUrl = cloudinaryService.uploadFile(
                        image,
                        folderName
                );

                existingUser.setImgUrl(userImageUrl);

            } catch (Exception e){
                e.printStackTrace();
                System.out.println(e);
                throw new RuntimeException("Cannot upload image");
            }
        }

        // update these necessary fields (as of now apr 19 12am 2026lol)
        existingUser.setFirstName(userDto.getFirstname());
        existingUser.setLastName(userDto.getLastname());
        existingUser.setUsername(userDto.getUsername());
        existingUser.setDisplayName(userDto.getDisplayName());

        return toDto(userRepository.save(existingUser));
    }

    @Override
    public UserDto saveUser(UserDto userDto){

        UserModel user = new UserModel();

        user.setFirstName(userDto.getFirstname());
        user.setLastName(userDto.getLastname());
        user.setEmailAddress(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setUserPassword(
                passwordEncoder.encode(userDto.getPassword())
        );
        user.setDisplayName(
                userDto.getDisplayName()
        );

        UserModel savedUser = userRepository.save(user);


        return toDto(savedUser);
    }
    @Cacheable
    @Override
    public List<UserDto> getUsers(String searchTerm, UUID userId){

        try{
            List<UserModel> fetchedUsers = userRepository.findUsers(searchTerm, userId);

            return fetchedUsers.stream().map(this::toDto).toList();

        } catch (Exception e) {
            log.info(String.valueOf(e));
            return new ArrayList<>();
        }
    }

    public UserDto toDto (UserModel entity){
        UserDto user = new UserDto();

        user.setUserId(entity.getUserId());
        user.setFirstname(entity.getFirstName());
        user.setLastname(entity.getLastName());
        user.setEmail(entity.getEmailAddress());
        user.setUsername(entity.getUsername());
        user.setDisplayName(entity.getDisplayName());
        user.setImgUrl(
                entity.getImgUrl()
        );
        user.setUserBio(
                entity.getUserBio()
        );

        return user;
    }


}
