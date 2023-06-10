package com.core.service;

import com.core.config.jwt.JwtUtils;
import com.core.config.security.UserDetailsImpl;
import com.core.dto.JwtResponse;
import com.core.dto.LoginRequestDTO;
import com.core.dto.UserDTO;
import com.core.entity.Role;
import com.core.entity.User;
import com.core.repository.RoleRepository;
import com.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository
            , RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<Object> signUp(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.getByRoleType(userDTO.getRoleType());
        if (role != null) {
            roles.add(role);
            user.setRoles(roles);
        }
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public JwtResponse signIn(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwtToken, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    public String deleteUser(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return "User deleted successfully";
        } else {
            return null;
        }
    }

}
