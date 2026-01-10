package com.nims.patient.service;

import com.nims.patient.client.AuthServiceClient;
import com.nims.patient.dto.PatientResponse;
import com.nims.patient.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final AuthServiceClient authServiceClient;

    // Admin → all patients
    public List<PatientResponse> getAllPatients() {
        return authServiceClient.getAllPatients(null, Role.PATIENT);
    }

    // Admin → patient by ID
    public PatientResponse getPatientById(Long id) {
        return authServiceClient.getPatientById(id, null);
    }

    // Patient → own profile by userId
    public PatientResponse getPatientByUserId(Long userId) {
        return null;
    }

    // Mapper
    /*private PatientResponse mapToDto(PatientProfile patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .dob(patient.getDob())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContact(patient.getEmergencyContact())
                .address(patient.getAddress())
                .build();
    }*/
}
