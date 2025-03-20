package com.vinfast.controller;

import com.vinfast.jwtutils.CustomUserDetails;
import com.vinfast.jwtutils.JwtUserDetailsService;
import com.vinfast.jwtutils.TokenManager;
import com.vinfast.models.JwtRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class JwtController {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;
   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody JwtRequestModel request) {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

      // Tạo access token & refresh token
      String accessToken = tokenManager.generateToken(userDetails);
      String refreshToken = tokenManager.generateRefreshToken(userDetails);

      // Gửi accessToken & refreshToken về FE
      return ResponseEntity.ok(Map.of(
              "accessToken", accessToken,
              "refreshToken", refreshToken,  // Gửi refreshToken thay vì cookie
              "user", Map.of(
                      "id", userDetails.getUserId(),
                      "username", userDetails.getUsername(),
                      "fullName", userDetails.getFullName(),
                      "role", userDetails.getRole()
              )
      ));
   }

}