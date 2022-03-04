package com.example.issuetracker.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Priority {
    CRITICAL, MAJOR, MINOR;

    public static Priority fromValue(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException();
    }
}
