package com.nims.auth.controller;

import com.nims.auth.dto.RegisterPatientRequest;
import com.nims.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register/patient")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerPatient(@Valid @RequestBody RegisterPatientRequest request) {
        userService.registerPatient(request);
        return "Patient registered successfully";
    }
}
