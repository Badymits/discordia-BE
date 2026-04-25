package com.example.discordia.service.CustomUser;

import java.util.Collection;
import java.util.List;

import com.example.discordia.model.UserModel;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails {

    private final UserModel userModel;


    public CustomUserDetails(UserModel userModel){
        this.userModel = userModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return userModel.getUserPassword();
    }

    @Override
    public @Nullable String getUsername(){
        return userModel.getUsername();
    }
}
