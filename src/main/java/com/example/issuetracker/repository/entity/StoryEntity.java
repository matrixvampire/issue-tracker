package com.example.issuetracker.repository.entity;

import com.example.issuetracker.domain.StoryStatus;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("STORY")
@Getter
@Setter
@NoArgsConstructor
public class StoryEntity extends IssueEntity {

    @Enumerated(EnumType.STRING)
    private StoryStatus status;
    private Integer storyPoint;
}
