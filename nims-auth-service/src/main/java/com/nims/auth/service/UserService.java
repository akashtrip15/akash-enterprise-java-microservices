package com.nims.auth.service;

import com.nims.auth.constants.ErrorMessages;
import com.nims.auth.constants.SuccessMessages;
import com.nims.auth.dto.*;
import com.nims.auth.entity.*;
import com.nims.auth.enums.AuthProvider;
import com.nims.auth.enums.ErrorCode;
import com.nims.auth.enums.Role;
import com.nims.auth.exception.ApiException;
import com.nims.auth.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String PROVIDER_ID = "NIMS";
    private static final String POSITION_DOCTOR = "Doctor";
    private static final String POSITION_ADMIN = "ADMIN";

    private final UserRepository userRepository;
    private final PersonProfileRepository personProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // -------------------- Patient --------------------

    @Transactional
    public String registerPatient(PatientRegisterRequest request) {
        validateUniqueUser(request.getEmail(), request.getContactNumber());

        User user = userRepository.save(buildUser(request, Set.of(Role.PATIENT)));
        PersonProfile person = personProfileRepository.save(buildPersonProfile(user, request));

        PatientProfile patient = PatientProfile.builder()
                .personProfile(person)
                .medicalRecordNumber("MRN-" + UUID.randomUUID())
                .build();
        patientProfileRepository.save(patient);

        saveAddresses(request.getAddresses(), person);

        log.info(SuccessMessages.PATIENT_REGISTERED + ": {}", user.getEmail());
        return SuccessMessages.PATIENT_REGISTERED;
    }

    // -------------------- Staff --------------------

    @Transactional
    public String onboardStaff(StaffRegisterRequest request) {
        User user = userRepository.save(buildUser(request, Set.of(Role.STAFF)));
        PersonProfile person = personProfileRepository.save(buildPersonProfile(user, request));

        StaffProfile staff = StaffProfile.builder()
                .personProfile(person)
                .employeeId("NIMS-" + UUID.randomUUID())
                .department(POSITION_ADMIN)
                .position(POSITION_ADMIN)
                .build();
        staffProfileRepository.save(staff);

        log.info(SuccessMessages.STAFF_REGISTERED + ": {}", user.getEmail());
        return SuccessMessages.STAFF_REGISTERED;
    }

    // -------------------- Doctor --------------------

    @Transactional
    public String onboardDoctor(DoctorOnboardRequest request) {
        User user = userRepository.save(buildUser(request, Set.of(Role.DOCTOR, Role.STAFF)));
        PersonProfile person = personProfileRepository.save(buildPersonProfile(user, request));

        StaffProfile staff = StaffProfile.builder()
                .personProfile(person)
                .employeeId("EMP-" + UUID.randomUUID())
                .department(request.getDepartment())
                .position(POSITION_DOCTOR)
                .build();
        staffProfileRepository.save(staff);

        DoctorProfile doctor = DoctorProfile.builder()
                .staffProfile(staff)
                .licenseNumber(request.getLicenseNumber())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .medicalDepartment(request.getDepartment())
                .build();
        doctorProfileRepository.save(doctor);

        log.info(SuccessMessages.DOCTOR_REGISTERED + ": {}", user.getEmail());
        return SuccessMessages.DOCTOR_REGISTERED;
    }

    @Transactional
    public String updateDoctorDetails(DoctorOnboardRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.DOCTOR_NOT_FOUND, ErrorMessages.DOCTOR_NOT_FOUND));

        user.setContactNumber(request.getContactNumber());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        PersonProfile person = personProfileRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.PERSON_PROFILE_NOT_FOUND, ErrorMessages.PERSON_PROFILE_NOT_FOUND));
        updatePersonProfile(person, request);
        personProfileRepository.save(person);

        StaffProfile staff = staffProfileRepository.findByPersonProfile(person)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.STAFF_PROFILE_NOT_FOUND, ErrorMessages.STAFF_PROFILE_NOT_FOUND));
        staff.setDepartment(request.getDepartment());
        staff.setPosition(POSITION_DOCTOR);
        staffProfileRepository.save(staff);

        DoctorProfile doctor = doctorProfileRepository.findByStaffProfile(staff)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.DOCTOR_PROFILE_NOT_FOUND, ErrorMessages.DOCTOR_PROFILE_NOT_FOUND));
        updateDoctorProfile(doctor, request);
        doctorProfileRepository.save(doctor);

        log.info(SuccessMessages.DOCTOR_UPDATED + ": {}", user.getEmail());
        return SuccessMessages.DOCTOR_UPDATED;
    }

    // -------------------- Queries --------------------

    public List<UserResponse> getAllUsersByRole(Role role) {
        List<User> users = userRepository.findByRoles(role);
        if (users.isEmpty()) {
            log.warn(ErrorMessages.USER_NOT_FOUND + " with role: {}", role.name());
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND, ErrorMessages.USER_NOT_FOUND + " with role " + role.name());
        }
        return users.stream()
                .map(u -> modelMapper.map(u, UserResponse.class))
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(u -> modelMapper.map(u, UserResponse.class))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND, ErrorMessages.USER_NOT_FOUND + " with id " + id));
    }

    public UserResponse getUserProfile(Long userId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // -------------------- Helpers --------------------

    private void validateUniqueUser(String email, String contactNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, ErrorCode.EMAIL_EXISTS, ErrorMessages.EMAIL_EXISTS);
        }
        if (userRepository.existsByContactNumber(contactNumber)) {
            throw new ApiException(HttpStatus.CONFLICT, ErrorCode.CONTACT_EXISTS, ErrorMessages.CONTACT_EXISTS);
        }
    }

    private User buildUser(BaseRegisterRequest request, Set<Role> roles) {
        return User.builder()
                .email(request.getEmail())
                .contactNumber(request.getContactNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .provider(AuthProvider.LOCAL)
                .providerId(PROVIDER_ID)
                .enabled(true)
                .build();
    }

    private PersonProfile buildPersonProfile(User user, BaseRegisterRequest request) {
        return PersonProfile.builder()
                .user(user)
                .fullName(request.getFullName())
                .dob(request.getDob())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .emergencyContact(request.getContactNumber())
                .build();
    }

    private void updatePersonProfile(PersonProfile person, BaseRegisterRequest request) {
        person.setFullName(request.getFullName());
        person.setDob(request.getDob());
        person.setGender(request.getGender());
        person.setBloodGroup(request.getBloodGroup());
        person.setEmergencyContact(request.getContactNumber());
    }

    private void updateDoctorProfile(DoctorProfile doctor, DoctorOnboardRequest request) {
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setMedicalDepartment(request.getDepartment());
    }

    private void saveAddresses(List<UserAddress> addressDtos, PersonProfile person) {
        if (addressDtos == null || addressDtos.isEmpty()) return;
        List<Address> addresses = addressDtos.stream()
                .map(dto -> modelMapper.map(dto, Address.class))
                .toList();
        addresses.forEach(adder -> adder.setPersonProfile(person));
        addressRepository.saveAll(addresses);
    }
}
