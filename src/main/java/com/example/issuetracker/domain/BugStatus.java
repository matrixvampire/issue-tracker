package com.example.issuetracker.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BugStatus {
    NEW, VERIFIED, RESOLVED;

    public static BugStatus fromValue(String value) {
        for (BugStatus bugStatus : BugStatus.values()) {
            if (bugStatus.name().equalsIgnoreCase(value)) {
                return bugStatus;
            }
        }
        throw new IllegalArgumentException();
    }
}
