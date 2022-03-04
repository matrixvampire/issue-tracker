package com.example.issuetracker.repository.entity;

import com.example.issuetracker.domain.BugStatus;
import com.example.issuetracker.domain.Priority;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("BUG")
@Getter
@Setter
@NoArgsConstructor
public class BugEntity extends IssueEntity {

    @Enumerated(EnumType.STRING)
    private BugStatus status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
}
