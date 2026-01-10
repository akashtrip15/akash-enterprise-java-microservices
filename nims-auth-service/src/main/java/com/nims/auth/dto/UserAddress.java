package com.nims.auth.dto;

import com.nims.auth.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddress implements Serializable {

    private AddressType type;

    @NotBlank
    private String line1;

    private String line2;
    private String city;
    private String state;
    private String zip;
    private String country;
}
