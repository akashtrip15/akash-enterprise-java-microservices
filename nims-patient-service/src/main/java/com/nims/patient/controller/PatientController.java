package com.nims.patient.controller;

import com.nims.patient.constant.ApiEndpoints;
import com.nims.patient.dto.PatientResponse;
import com.nims.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ApiEndpoints.PATIENT_BASE)
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // ===========================
    // Admin → View all patients
    // ===========================
    @GetMapping(ApiEndpoints.ALL_PATIENTS)
    public List<PatientResponse> getAllPatients() {
        log.info("Request received....getAllPatients");
        return patientService.getAllPatients();
    }

    // ===========================
    // Admin → Fetch patient by ID
    // ===========================
    @GetMapping(ApiEndpoints.PATIENT_BY_ID)
    public PatientResponse getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    // ===========================
    // Patient → View own profile
    // AuthenticationPrincipal injects logged-in userId (from JWT)
    // ===========================
   /* @GetMapping(ApiEndpoints.PATIENT_DETAILS)
    public PatientResponse getMyProfile(@AuthenticationPrincipal Long userId) {
        return patientService.getPatientByUserId(userId);
    }*/
}
