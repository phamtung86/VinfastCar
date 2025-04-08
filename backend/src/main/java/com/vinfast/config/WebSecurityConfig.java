package com.vinfast.config;

import com.vinfast.jwtutils.JwtAuthenticationEntryPoint;
import com.vinfast.jwtutils.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

         .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))

         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 

         .addFilterBefore(filter,  UsernamePasswordAuthenticationFilter.class)
         .build();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowCredentials(true);
      configuration.setAllowedOrigins(List.of("http://localhost:3000"));
      configuration.setAllowedHeaders(List.of("*"));
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      configuration.addExposedHeader("Authorization");
      configuration.addExposedHeader("Set-Cookie");
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }
}