package com.nims.auth.constants;


public final class ApiEndpoints {

    private ApiEndpoints() {
    }

    public static final String AUTH_BASE = "/api/auth";

    // Login Controller
    public static final String LOGIN = "/login";
    public static final String REFRESH = "/refresh";
    public static final String LOGOUT = "/logout";
    public static final String WELL_KNOWN_JWKS_JSON = "/.well-known/jwks.json";
    //Auth Controller
    public static final String REGISTER_PATIENT = "/register/patient";
    public static final String REGISTER_STAFF = "/register/staff";
    public static final String REGISTER_DOCTOR = "/register/doctor";
    public static final String FIND_ALL_USERS_BY_ROLE = "/users/all";
    public static final String GET_USER_BY_ID = "/user/{id}";
    public static final String GET_USER_PROFILE = "/user/profile";


    public static final String DOCTOR_BASE = "/api/doctors";

}

