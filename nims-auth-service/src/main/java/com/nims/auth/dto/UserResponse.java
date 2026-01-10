package com.nims.auth.dto;

import com.nims.auth.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    private Long id;
    private String fullName;
    private String email;
    private LocalDate dob;
    private Gender gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
}
