package com.example.issuetracker.util;

import com.example.issuetracker.controller.response.BugResponse;
import com.example.issuetracker.controller.response.DeveloperResponse;
import com.example.issuetracker.controller.response.IssueResponse;
import com.example.issuetracker.controller.response.PlanResponse;
import com.example.issuetracker.controller.response.StoryResponse;
import com.example.issuetracker.repository.entity.BugEntity;
import com.example.issuetracker.repository.entity.DeveloperEntity;
import com.example.issuetracker.repository.entity.StoryEntity;
import java.util.List;
import java.util.Objects;

public final class ResponseBuilderUtil {

    private ResponseBuilderUtil() {
    }

    public static StoryResponse buildStoryResponse(StoryEntity storyEntity) {
        StoryResponse storyResponse = new StoryResponse();
        storyResponse.setId(storyEntity.getId());
        storyResponse.setTitle(storyEntity.getTitle());
        storyResponse.setDescription(storyEntity.getDescription());
        storyResponse.setCreationDate(storyEntity.getCreationDate());
        storyResponse.setStatus(storyEntity.getStatus().name());
        storyResponse.setStoryPoint(storyEntity.getStoryPoint());
        if (Objects.nonNull(storyEntity.getDeveloper())) {
            storyResponse.setDeveloper(buildDeveloperResponse(storyEntity.getDeveloper()));
        }
        return storyResponse;
    }

    public static BugResponse buildBugResponse(BugEntity bugEntity) {
        BugResponse bugResponse = new BugResponse();
        bugResponse.setId(bugEntity.getId());
        bugResponse.setTitle(bugEntity.getTitle());
        bugResponse.setDescription(bugEntity.getDescription());
        bugResponse.setCreationDate(bugEntity.getCreationDate());
        bugResponse.setStatus(bugEntity.getStatus().name());
        bugResponse.setPriority(bugEntity.getPriority().name());
        if (Objects.nonNull(bugEntity.getDeveloper())) {
            bugResponse.setDeveloper(buildDeveloperResponse(bugEntity.getDeveloper()));
        }
        return bugResponse;
    }

    public static DeveloperResponse buildDeveloperResponse(DeveloperEntity developerEntity) {
        return new DeveloperResponse(developerEntity.getId(), developerEntity.getName());
    }

    public static PlanResponse buildPlanResponse(Integer week, List<IssueResponse> issueResponses) {
        return new PlanResponse(week, issueResponses);
    }

    public static IssueResponse buildIssueResponse(StoryEntity storyEntity) {
        IssueResponse issueResponse = new IssueResponse();
        issueResponse.setId(storyEntity.getId());
        issueResponse.setType(storyEntity.getType().name());
        issueResponse.setTitle(storyEntity.getTitle());
        issueResponse.setDescription(storyEntity.getDescription());
        issueResponse.setStoryPoint(storyEntity.getStoryPoint());
        issueResponse.setCreationDate(storyEntity.getCreationDate());
        issueResponse.setStatus(storyEntity.getStatus().name());
        if (Objects.nonNull(storyEntity.getDeveloper())) {
            issueResponse.setDeveloper(buildDeveloperResponse(storyEntity.getDeveloper()));
        }
        return issueResponse;
    }
}
