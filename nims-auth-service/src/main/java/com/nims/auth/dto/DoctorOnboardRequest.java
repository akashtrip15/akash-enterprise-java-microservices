package com.nims.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DoctorOnboardRequest extends BaseRegisterRequest {
    private String specialization;
    private String licenseNumber;
    Integer experienceYears;
    private String department;
}
