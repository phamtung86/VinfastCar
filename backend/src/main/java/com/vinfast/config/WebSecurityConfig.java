package com.vinfast.config;

import com.vinfast.jwtutils.JwtAuthenticationEntryPoint;
import com.vinfast.jwtutils.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration 
@EnableWebSecurity
public class WebSecurityConfig {

   @Autowired
   private JwtAuthenticationEntryPoint authenticationEntryPoint;
   @Autowired
   private JwtFilter filter;

   @Bean 
   protected PasswordEncoder passwordEncoder() { 
      return new BCryptPasswordEncoder(); 
   }

   @Bean
   protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
      return http
         .csrf(AbstractHttpConfigurer::disable)
         .authorizeHttpRequests(request -> request.requestMatchers("/login").permitAll()
         .anyRequest().authenticated())
         // Send a 401 error response if user is not authentic.		 
         .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
         // no session management
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
         // filter the request and add authentication token		 
         .addFilterBefore(filter,  UsernamePasswordAuthenticationFilter.class)
         .build();
   }

   @Bean
   AuthenticationManager customAuthenticationManager() {
      return authentication -> new UsernamePasswordAuthenticationToken("randomuser123","password");
   }
}