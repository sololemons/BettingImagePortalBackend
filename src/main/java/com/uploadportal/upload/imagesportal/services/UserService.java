package com.uploadportal.upload.imagesportal.services;

import com.uploadportal.upload.imagesportal.entities.Users;
import com.uploadportal.upload.imagesportal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
