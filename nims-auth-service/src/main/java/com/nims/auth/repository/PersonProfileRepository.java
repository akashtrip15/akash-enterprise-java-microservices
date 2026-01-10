package com.nims.auth.repository;

import com.nims.auth.entity.PersonProfile;
import com.nims.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonProfileRepository extends JpaRepository<PersonProfile, Long> {
    Optional<PersonProfile> findByUser(User user);
}