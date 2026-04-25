package com.example.discordia.service.CustomUser;

import com.example.discordia.exception.EmailNotFound;
import org.springframework.stereotype.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.discordia.model.UserModel;
import com.example.discordia.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;


@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        UserModel userModel = userRepository.findByEmailAddress(email);

        if (userModel == null) throw new EmailNotFound("Username with email: %s not found".formatted(email));

        return new CustomUserDetails(userModel);
    }

    public Collection<? extends GrantedAuthority> authorities(){
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

}
