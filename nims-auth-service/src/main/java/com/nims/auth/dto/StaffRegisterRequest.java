package com.nims.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StaffRegisterRequest extends BaseRegisterRequest {
    private String department;
    private String position;
}
