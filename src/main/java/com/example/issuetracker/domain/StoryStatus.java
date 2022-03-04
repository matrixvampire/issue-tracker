package com.example.issuetracker.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StoryStatus {
    NEW, ESTIMATED, COMPLETED;

    public static StoryStatus fromValue(String value) {
        for (StoryStatus storyStatus : StoryStatus.values()) {
            if (storyStatus.name().equalsIgnoreCase(value)) {
                return storyStatus;
            }
        }
        throw new IllegalArgumentException();
    }
}
