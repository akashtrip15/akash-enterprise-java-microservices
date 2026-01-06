package com.nims.auth.service;

import com.nims.auth.dto.LoginRequest;
import com.nims.auth.dto.LoginResponse;
import com.nims.auth.dto.RegisterPatientRequest;
import com.nims.auth.entity.PatientProfile;
import com.nims.auth.entity.User;
import com.nims.auth.enums.Role;
import com.nims.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;

    public void registerPatient(RegisterPatientRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                //.password(passwordEncoder.encode(request.getPassword()))
                .dob(request.getDob())
                .gender(request.getGender())
                .role(Role.PATIENT)
                .build();

        userRepository.save(user);

        PatientProfile profile = PatientProfile.builder()
                .user(user)
                .bloodGroup(request.getBloodGroup())
                .emergencyContact(request.getEmergencyContact())
                .address(request.getAddress())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

       /* if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getId().toString(), user.getRole().name());

        return new LoginResponse(token);*/
        return null;
    }
}
