package com.airbnb.be.api;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ApiConstants {

    public static final String APP_NAME = "AIRBNB_MANAGER";
    public static final String PING = "/ping";
    public static final String HEALTH = "/app-health";
    public static final String HEALTH_URL_REDIRECT = "/actuator/health";
    public static final String PING_RESPONSE = " >> PING SUCCESS";
    public static final String CHANNEL = "channel";
    public static final String APPLICATIONS= "applications";
    public static final String APPLICATION = "XApplication";
    public static final String TRANS_ID = "transactionID";
    public static final String IDENTIFIER = "identifier";
    public static final String ID_TYPE = "idType";
    public static final String USER_ID = "userId";
    public static final String ERRORS = "errors";
    public static final String SUCCESS = "SUCCESS";
    public static final String SUCCESS_VALUE = "200";
    public static final String ID = "Id";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    //logging
    public static final String ABM_API_REQ_LOGGER = "{}: ABM request received with: {}";
    public static final String ABM_API_RES_LOGGER = "{}: ABM API request completed";
    public static final String ABM_API_ERR_LOGGER = "{}: ABM API request failed with error: {}";
    public static final String MONGO_DB_ERR_LOGGER = "{}: MongoDB Request failed with error: {}";
    public static final String MONGO_DB_LOGGER = "{}: MongoDB Request Completed";
    public static final String MONGO_DB_SEARCH_LOGGER = "{}: MongoDB Search Completed";
    public static final String DATA_INIT_COMPLETE = "lookup data initialization completed";
    public static final String DATA_INIT_FAILURE = "lookup data initialization failed";


}
