package com.example.issuetracker.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public enum ResponseMessageEnum {

    SUCCESS("Success"),
    DEVELOPER_NOT_FOUND("Developer not found"),
    STORY_NOT_FOUND("Story not found"),
    BUG_NOT_FOUND("Bug not found");

    private final String message;
}
