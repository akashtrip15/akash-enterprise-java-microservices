package com.nims.auth.repository;

import com.nims.auth.entity.PersonProfile;
import com.nims.auth.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
    Optional<StaffProfile> findByPersonProfile(PersonProfile person);
}