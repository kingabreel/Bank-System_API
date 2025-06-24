package com.gab.apibank_system.controller;

import com.gab.apibank_system.domain.dto.Login;
import com.gab.apibank_system.domain.model.User;
import com.gab.apibank_system.domain.request.UserRequest;
import com.gab.apibank_system.domain.response.Response;
import com.gab.apibank_system.domain.response.UserResponse;
import com.gab.apibank_system.infra.security.TokenService;
import com.gab.apibank_system.repository.UserRepository;
import com.gab.apibank_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * Class that holds the users and accounts endpoints.
 *
 * @author kingabreel
 * @since 07-2025
 */
@RestController
@RequestMapping("/v1/account")
public class AccountController implements Serializable {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public AccountController(UserRepository repository, AuthenticationManager authenticationManager, TokenService tokenService, UserService userService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getTest() {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<Response<UserResponse>> createAccount(@RequestBody UserRequest data) {
        Response<UserResponse> finalResponse = new Response<>();
        try {
            if (this.repository.findByEmail(data.email()) != null) {
                return ResponseEntity.badRequest().build();
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

            User user = User.fromDto(data, encryptedPassword);

            this.repository.save(user);

            UserResponse userRes = userService.configUser(user);

            finalResponse.setSuccess(userRes, "Account created successfully", HttpStatus.CREATED.toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(finalResponse);
        } catch (Exception e) {

            finalResponse.setError(e.getMessage(), HttpStatus.BAD_REQUEST.toString());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(finalResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody Login data){
        Response<String> finalResponse = new Response<>();

        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            finalResponse.setSuccess(token, "Logged successfully", HttpStatus.OK.toString());

            return ResponseEntity.status(HttpStatus.OK).body(finalResponse);
        } catch (Exception e) {
            finalResponse.setError(e.getMessage(), HttpStatus.BAD_REQUEST.toString());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(finalResponse);
        }
    }
}
