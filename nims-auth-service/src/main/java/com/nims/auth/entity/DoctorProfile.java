package com.nims.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String specialization;

    private String licenseNumber;

    private Integer experienceYears;

    private String department;
}
