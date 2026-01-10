package com.nims.auth.repository;

import com.nims.auth.entity.DoctorProfile;
import com.nims.auth.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByStaffProfile(StaffProfile staff);
}