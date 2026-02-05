package com.airbnb.be.api;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ApiConstants {

    public static final String PING = "/ping";
    public static final String HEALTH = "/app-health";
    public static final String HEALTH_URL_REDIRECT = "/actuator/health";
    public static final String PING_RESPONSE = " >> PING SUCCESS";
    public static final String CHANNEL = "channel";
    public static final String APPLICATION = "application";
    public static final String TRANS_ID = "transactionID";
    public static final String IDENTIFIER = "identifier";
    public static final String ID_TYPE = "idType";
    public static final String ERRORS = "errors";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_INIT_COMPLETE = "lookup data initialization completed";
    public static final String DATA_INIT_FAILURE = "lookup data initialization failed";



}
