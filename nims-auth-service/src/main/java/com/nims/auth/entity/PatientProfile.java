package com.nims.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient_profiles", schema = "nims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private PersonProfile personProfile;

    @Column(nullable = false, unique = true)
    private String medicalRecordNumber;

    private String insuranceProvider;
}
