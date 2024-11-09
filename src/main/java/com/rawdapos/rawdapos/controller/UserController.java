package com.rawdapos.rawdapos.controller;

import java.time.Instant;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.rawdapos.rawdapos.models.Users;
import com.rawdapos.rawdapos.payload.LoginRequest;
import com.rawdapos.rawdapos.payload.UserRequest;
import com.rawdapos.rawdapos.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;    

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
   
    public UserController(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication authentication){
        var response = new HashMap<String, Object>();
        response.put("Username", authentication.getName());
        response.put("Authorities", authentication.getAuthorities());

        var user = userRepository.findByUsername(authentication.getName());
        response.put("User", user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(
        @Valid @RequestBody UserRequest userRegistration,
        BindingResult result
    ){
        if (result.hasErrors()){
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String,String>();

            for (int i = 0; i < errorsList.size(); i++){
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        
        var bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Users user = new Users();
        user.setFirstName(userRegistration.getFirstName());
        user.setLastName(userRegistration.getLastName());
        user.setUsername(userRegistration.getUsername());
        user.setEmail(userRegistration.getEmail());
        user.setPhone(userRegistration.getPhone());
        user.setAddresse(userRegistration.getAddresse());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistration.getPassword()));
        user.setRole("USER");



        try {
            var otherUser = userRepository.findByUsername(userRegistration.getUsername());
            if (otherUser != null){
                return ResponseEntity.badRequest().body("Username already exists");
            }

            otherUser = userRepository.findByEmail(userRegistration.getEmail());
            if (otherUser != null){
                return ResponseEntity.badRequest().body("Email already exists");
            }
            userRepository.save(user);

            String jwtToken = createJwtToken(user);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("An error occurred");
    }

    private String createJwtToken(Users user){
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(jwtIssuer)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(24 * 3600))
            .subject(user.getUsername())
            .build();

        SecretKeySpec key = new SecretKeySpec(jwtSecretKey.getBytes(), "HmacSHA256");
        var encoder = new NimbusJwtEncoder(
            new ImmutableSecret<>(key)
        );
        var params =  JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(),
            claims
        );
        return encoder.encode(params).getTokenValue();
        
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
        @Valid @RequestBody LoginRequest userLogin,
        BindingResult result
    )
    {
        if(result.hasErrors()){
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String,String>();
            for (int i = 0; i < errorsList.size(); i++){
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userLogin.getUsername(),
                    userLogin.getPassword()
                )
            );

            var user = userRepository.findByUsername(userLogin.getUsername());
            String jwtToken = createJwtToken(user);
            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", user);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }
    
    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/add")
    public String adminAccess() {
        return "Admin Board.";
    }
       
}
