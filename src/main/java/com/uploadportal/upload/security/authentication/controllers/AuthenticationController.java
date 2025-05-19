package com.uploadportal.upload.security.authentication.controllers;


import com.uploadportal.upload.imagesportal.dtos.ChangePasswordRequest;
import com.uploadportal.upload.security.authentication.entities.AuthenticationRequest;
import com.uploadportal.upload.security.authentication.entities.AuthenticationResponse;
import com.uploadportal.upload.security.authentication.entities.RegisterRequest;
import com.uploadportal.upload.security.authentication.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PutMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        String username = principal.getName();
        boolean success = service.changePassword(username, request);

        if (success) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current password");
        }
    }
}


