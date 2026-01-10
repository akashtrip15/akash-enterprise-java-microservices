package com.nims.patient.dto;


import com.nims.patient.enums.Gender;
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
public class PatientResponse implements Serializable {

    private Long id;
    private String fullName;
    private String email;
    private LocalDate dob;
    private Gender gender;
    private String bloodGroup;
    private String address;
    private String emergencyContact;
}

