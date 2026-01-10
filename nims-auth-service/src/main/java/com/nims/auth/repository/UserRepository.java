package com.nims.auth.repository;

import com.nims.auth.entity.User;
import com.nims.auth.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByContactNumber(String contactNumber);

    List<User> findByRoles(Role role);

}
