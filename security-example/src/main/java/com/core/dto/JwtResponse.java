package com.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {

    private String token;

    private long id;

    private String username;

    private String email;

    private List<String> roles;

    public JwtResponse(String token, long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
