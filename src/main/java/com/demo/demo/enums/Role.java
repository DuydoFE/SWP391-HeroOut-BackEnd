package com.demo.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    MEMBER,
    ADMIN,
    CONSULTANT,
    STAFF;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
