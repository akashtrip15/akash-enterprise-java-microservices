package com.nims.auth.controller;

import com.nims.auth.constants.ApiEndpoints;
import com.nims.auth.dto.DoctorOnboardRequest;
import com.nims.auth.dto.PatientRegisterRequest;
import com.nims.auth.dto.StaffRegisterRequest;
import com.nims.auth.dto.UserResponse;
import com.nims.auth.enums.Role;
import com.nims.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.nims.auth.constants.ApiEndpoints.*;

@Slf4j
@RestController
@RequestMapping(AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(ApiEndpoints.REGISTER_PATIENT)
    @ResponseStatus(HttpStatus.CREATED)
    public String registerPatient(@Valid @RequestBody PatientRegisterRequest request) {
        return userService.registerPatient(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiEndpoints.REGISTER_STAFF)
    @PreAuthorize("hasRole('ADMIN')")
    public String onboardStaff(@RequestBody StaffRegisterRequest request) {
        return userService.onboardStaff(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ApiEndpoints.REGISTER_DOCTOR)
    public String onboardDoctor(@RequestBody DoctorOnboardRequest request) {
        return userService.onboardDoctor(request);
    }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(ApiEndpoints.REGISTER_DOCTOR)
    public String updateDoctorDetails(@RequestBody DoctorOnboardRequest request) {
        return userService.updateDoctorDetails(request);
    }

    @GetMapping(FIND_ALL_USERS_BY_ROLE)
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsersByRole(@RequestParam Role role) {
        log.info("Request received for getAllUsersByRole role: {}", role.name());
        return userService.getAllUsersByRole(role);
    }

    @GetMapping(GET_USER_BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUsersById(@RequestParam Long userId) {
        return userService.getUserById(userId);
    }

}
