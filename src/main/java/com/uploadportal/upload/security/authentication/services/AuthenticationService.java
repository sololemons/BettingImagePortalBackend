package com.uploadportal.upload.security.authentication.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.uploadportal.upload.imagesportal.dtos.ChangePasswordRequest;
import com.uploadportal.upload.imagesportal.entities.Users;
import com.uploadportal.upload.imagesportal.exceptions.MissingFieldException;
import com.uploadportal.upload.imagesportal.exceptions.UserNotFoundException;
import com.uploadportal.upload.imagesportal.repositories.UserRepository;
import com.uploadportal.upload.security.authentication.entities.AuthenticationRequest;
import com.uploadportal.upload.security.authentication.entities.AuthenticationResponse;
import com.uploadportal.upload.security.authentication.entities.RegisterRequest;
import com.uploadportal.upload.security.configuration.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository repository;
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        String formattedPhoneNumber = validateAndFormatPhoneNumber(request.getPhoneNumber().trim());
        String password = request.getPassword().trim();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        formattedPhoneNumber,
                        password

                )
        );
        var user = repository.findByPhoneNumber(formattedPhoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found with phone number: " + formattedPhoneNumber));
        var jwtToken = jwtService.generateAuthenticationToken(user);

        return AuthenticationResponse.builder().
                token(jwtToken)
                .build();

    }

    public AuthenticationResponse register(RegisterRequest request) {

        String formattedPhoneNumber = validateAndFormatPhoneNumber(request.getPhoneNumber());

        String password = request.getPassword().trim();
        String confirmPassword = request.getConfirmPassword().trim();


        if (!password.equals(confirmPassword)) {
            throw new MissingFieldException("Passwords do not match!");
        }
        if (repository.findByPhoneNumber(formattedPhoneNumber).isPresent()) {
            throw new MissingFieldException("Phone number is already registered!");
        }
        var user = Users.builder()
                .phoneNumber(formattedPhoneNumber)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateRegistrationToken(user);

        return AuthenticationResponse.builder().
                token(jwtToken)
                .build();

    }

    private String validateAndFormatPhoneNumber(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, "KE");
            if (!phoneUtil.isValidNumber(number)) {
                throw new MissingFieldException("Invalid phone number!");
            }
            if (number.getCountryCode() != 254) {
                throw new MissingFieldException("Only Kenyan phone numbers (+254) are allowed!");
            }


            return phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

        } catch (NumberParseException e) {
            throw new MissingFieldException("Invalid phone number format or it is null!");
        }
    }
    public boolean changePassword(String username, ChangePasswordRequest request) {
        Optional<Users> userOpt = repository.findByPhoneNumber(username);
        if (userOpt.isEmpty()) return false;

        Users user = userOpt.get();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        return true;
    }
}

