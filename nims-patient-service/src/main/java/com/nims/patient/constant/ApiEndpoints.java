package com.nims.patient.constant;

public class ApiEndpoints {

    //Patient Service Endpoints
    public static final String PATIENT_BASE = "/api/patient";
    public static final String ALL_PATIENTS = "/all"; // admin
    public static final String PATIENT_BY_ID = "/{id}"; // admin fetch specific patient
    public static final String PATIENT_DETAILS = "/me"; // logged-in patient

    //Auth Service Endpoints
    public static final String AUTH_FIND_ALL_PATIENTS_BY_ROLE = "/api/auth/users";
    public static final String AUTH_GET_PATIENT_BY_ID = "/api/auth/user/{id}";
    public static final String AUTH_GET_PATIENT_PROFILE = "/api/auth/user/profile";


}
