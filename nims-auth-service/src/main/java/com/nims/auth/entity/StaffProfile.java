package com.nims.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staff_profiles", schema = "nims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private PersonProfile personProfile;

    @Column(nullable = false, unique = true)
    private String employeeId;

    private String department;

    private String position;
}
