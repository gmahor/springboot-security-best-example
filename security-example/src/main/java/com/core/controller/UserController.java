package com.core.controller;

import com.core.config.UserDetailsImpl;
import com.core.config.jwt.JwtUtils;
import com.core.dto.JwtResponse;
import com.core.dto.LoginRequestDTO;
import com.core.dto.UserDTO;
import com.core.entity.User;
import com.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new JwtResponse(jwtToken,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while signin user : ", e);
            return new ResponseEntity<>("Error while signin user", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO) {
        try {
            log.info("(CONTROLLER)Signup request received by userDto : {}", userDTO);
            return userService.signUp(userDTO);
        } catch (Exception e) {
            log.error("Error while register user : ", e);
            return new ResponseEntity<>("Error while register user!", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getUsers() {
        try {
            List<User> users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while getting users : ", e);
            return new ResponseEntity<>("Error while getting users.", HttpStatus.BAD_REQUEST);
        }
    }

}
