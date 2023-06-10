package com.core.controller;

import com.core.dto.JwtResponse;
import com.core.dto.LoginRequestDTO;
import com.core.dto.UserDTO;
import com.core.entity.User;
import com.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            JwtResponse jwtResponse = userService.signIn(loginRequestDTO);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while signIn user : ", e);
            return new ResponseEntity<>("Error while signIn user", HttpStatus.BAD_REQUEST);
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
