package com.shop.constant;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class ViewModel {

    public final static String USER_ID = "USER_ID";
    public final static String IS_EMPTY = "IS_EMPTY";

    public static String getUserName() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return "hidden";
        } else {
            if("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName().trim())) {
                return null;
            } else {
                return SecurityContextHolder.getContext().getAuthentication().getName();
            }
        }
    }
}
