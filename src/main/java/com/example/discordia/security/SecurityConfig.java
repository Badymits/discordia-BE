package com.example.discordia.security;

import com.example.discordia.service.CustomUser.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService userDetailsService;


    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                // Enable CORS to allow requests from other origins (like React frontend at localhost:3000)
                .cors(Customizer.withDefaults())

                // Disable CSRF protection since we're handling authentication from a separate frontend (React)
                // and likely using token/session manually if needed
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests((requests) -> requests

                    // any request coming from unknown sources will be redirected to these endpoints
                    // meaning any unauthenticated requests will go through these first
                    .requestMatchers(
                            "/api/users/register",
                            "/api/users/login",
                            "/api/users/all-users",
                            "/api/users/**",
                            "/actuator/**",
                            "/api/server/**",
                            "/api/channels/**",
                            "/api/category/**",
                            "/api/members/**",
                            "/api/messages/**",
                            "/ws/**"// temporary fix since we're not using any cookies atm (mar 21 1:08am)
                    ).permitAll()
                    // All other requests require authentication
                    .anyRequest().authenticated()
                )

                .userDetailsService(userDetailsService)
                .authenticationProvider(authenticationProvider())

                //  anyone (even unauthenticated users) is allowed to call the logout endpoint
                .logout(LogoutConfigurer::permitAll);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // Specifies which origins are allowed to access this server.
        // In development, React runs on http://localhost:5173, so we explicitly allow it.
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Define the HTTP methods allowed when accessing resources (GET, POST, etc.)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        // Allows all headers to be included in the requests (like Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(List.of("*"));

        // 🛡Allows sending of cookies and authentication credentials with the request (important for login sessions)
        configuration.setAllowCredentials(true);

        // Registers this CORS configuration for all paths in the application (/** = apply to every route)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        // Returns the configured CORS source to be used by Spring Security
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}
