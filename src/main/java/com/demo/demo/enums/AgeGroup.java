package com.demo.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AgeGroup {
    CHILDREN,       // Trẻ em (dưới 12 tuổi)
    TEENAGERS,      // Thiếu niên (13–17 tuổi)
    YOUNG_ADULTS,   // Thanh niên (18–25 tuổi)
    ADULTS,         // Người lớn (26–59 tuổi)
    SENIORS;        // Người cao tuổi (60+)

    @JsonCreator
    public static AgeGroup fromString(String value) {
        return AgeGroup.valueOf(value.toUpperCase());
    }
}