package com.uploadportal.upload.imagesportal.controllers;

import com.uploadportal.upload.imagesportal.entities.Users;
import com.uploadportal.upload.imagesportal.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    public ResponseEntity<List<Users>> getAllUsers() {
            return ResponseEntity.ok(userService.getAllUsers());
    }
}
