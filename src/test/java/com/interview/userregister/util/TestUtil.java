package com.interview.userregister.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    public static String stringifyObject(Object object) {
        String result = null;
        if (null != object) {
            try {
                result = new ObjectMapper().writeValueAsString(object);
            } catch (Exception e) {
                result = object.toString();
            }
        }
        return result;
    }
}
