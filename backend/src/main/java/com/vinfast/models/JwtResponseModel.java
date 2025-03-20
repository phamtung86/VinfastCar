package com.vinfast.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JwtResponseModel implements Serializable {
   private static final long serialVersionUID = 1L;
   private String token;
   private String username;
   private Integer userId;
}
