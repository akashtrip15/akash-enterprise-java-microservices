package com.nims.auth.dto;

import com.nims.auth.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
public class RegisterPatientRequest {

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Past
    private LocalDate dob;

    private Gender gender;

    private String bloodGroup;

    private String emergencyContact;

    private String address;
}
