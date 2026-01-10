package com.nims.patient.client;

import com.nims.patient.constant.ApiEndpoints;
import com.nims.patient.dto.PatientResponse;
import com.nims.patient.enums.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "nims-auth-service")
public interface AuthServiceClient {

    // Admin → all patients
    @GetMapping(ApiEndpoints.AUTH_FIND_ALL_PATIENTS_BY_ROLE)
    List<PatientResponse> getAllPatients(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("role") Role role
    );

    // Admin → patient by id
    @GetMapping(ApiEndpoints.AUTH_GET_PATIENT_BY_ID)
    PatientResponse getPatientById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    );

    // Patient → own profile
    @GetMapping("/api/internal/patients/me")
    PatientResponse getMyProfile(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String authorization
    );

}
