package com.nims.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_profile", schema = "nims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false, unique = true)
    private StaffProfile staffProfile;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String specialization;

    private Integer experienceYears;

    private String medicalDepartment;
}
