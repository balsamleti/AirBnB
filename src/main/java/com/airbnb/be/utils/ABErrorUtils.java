package com.airbnb.be.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ABErrorUtils {

    public static Integer parseError(String errString) {
        if (errString.equals("id empty")) {
            return 100001;
        } else if (errString.equals("body empty")) {
            return 100002;
        }
        return 10014;
    }

}
