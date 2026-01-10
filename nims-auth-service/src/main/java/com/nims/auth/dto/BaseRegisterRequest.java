package com.nims.auth.dto;

import com.nims.auth.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BaseRegisterRequest {

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @NotBlank
    private String contactNumber;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private LocalDate dob;

    @NotBlank
    private Gender gender;

    private String bloodGroup;

    private List<UserAddress> addresses;

}