package com.nims.auth.entity;

import com.nims.auth.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address", schema = "nims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AddressType type;

    @Column(nullable = false)
    private String line1;

    private String line2;
    private String city;
    private String state;
    private String zip;
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonProfile personProfile;
}
