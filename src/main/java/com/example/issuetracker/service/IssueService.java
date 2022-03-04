package com.example.issuetracker.service;

import static com.example.issuetracker.util.ResponseBuilderUtil.buildBugResponse;
import static com.example.issuetracker.util.ResponseBuilderUtil.buildStoryResponse;

import com.example.issuetracker.controller.request.AssigneeRequest;
import com.example.issuetracker.controller.request.BugCreateRequest;
import com.example.issuetracker.controller.request.BugUpdateRequest;
import com.example.issuetracker.controller.request.StoryCreateRequest;
import com.example.issuetracker.controller.request.StoryUpdateRequest;
import com.example.issuetracker.controller.response.BugResponse;
import com.example.issuetracker.controller.response.StoryResponse;
import com.example.issuetracker.domain.BugStatus;
import com.example.issuetracker.domain.Priority;
import com.example.issuetracker.domain.ResponseMessageEnum;
import com.example.issuetracker.domain.StoryStatus;
import com.example.issuetracker.exception.BadRequestException;
import com.example.issuetracker.repository.BugRepository;
import com.example.issuetracker.repository.DeveloperRepository;
import com.example.issuetracker.repository.StoryRepository;
import com.example.issuetracker.repository.entity.BugEntity;
import com.example.issuetracker.repository.entity.StoryEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IssueService {

    private final StoryRepository storyRepository;
    private final BugRepository bugRepository;
    private final DeveloperRepository developerRepository;

    public StoryResponse createStory(StoryCreateRequest storyCreateRequest) {
        log.debug("Create story with request [{}]", storyCreateRequest);
        var storyEntity = storyRepository.save(buildStoryEntity(storyCreateRequest));
        log.info("Story created with id [{}]", storyEntity.getId());
        return buildStoryResponse(storyEntity);
    }

    public StoryResponse getStory(Integer id) {
        log.debug("Get story by id [{}]", id);
        var storyEntity = storyRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.STORY_NOT_FOUND));
        return buildStoryResponse(storyEntity);
    }

    public StoryResponse updateStory(Integer id, StoryUpdateRequest storyUpdateRequest) {
        log.debug("Update story by id [{}] with request [{}]", id, storyUpdateRequest);
        var storyEntity = storyRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.STORY_NOT_FOUND));
        storyEntity = storyRepository.save(buildStoryEntity(storyEntity, storyUpdateRequest));
        log.info("Story with id [{}] updated", id);
        return buildStoryResponse(storyEntity);
    }

    public void deleteStory(Integer id) {
        log.debug("Delete story by id [{}]", id);
        boolean exists = storyRepository.existsById(id);
        if (!exists) {
            throw new BadRequestException(ResponseMessageEnum.STORY_NOT_FOUND);
        }
        storyRepository.deleteById(id);
        log.info("Story with id [{}] deleted", id);
    }

    public BugResponse createBug(BugCreateRequest bugCreateRequest) {
        log.debug("Create bug with request [{}]", bugCreateRequest);
        var bugEntity = bugRepository.save(buildBugEntity(bugCreateRequest));
        log.info("Bug created with id [{}]", bugEntity.getId());
        return buildBugResponse(bugEntity);
    }

    public BugResponse getBug(Integer id) {
        log.debug("Get bug by id [{}]", id);
        var bugEntity = bugRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.BUG_NOT_FOUND));
        return buildBugResponse(bugEntity);
    }

    public BugResponse updateBug(Integer id, BugUpdateRequest bugUpdateRequest) {
        log.debug("Update bug by id [{}] with request [{}]", id, bugUpdateRequest);
        var bugEntity = bugRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.BUG_NOT_FOUND));
        bugEntity = bugRepository.save(buildBugEntity(bugEntity, bugUpdateRequest));
        log.info("Bug with id [{}] updated", id);
        return buildBugResponse(bugEntity);
    }

    public void deleteBug(Integer id) {
        log.debug("Delete bug by id [{}]", id);
        boolean exists = bugRepository.existsById(id);
        if (!exists) {
            throw new BadRequestException(ResponseMessageEnum.BUG_NOT_FOUND);
        }
        bugRepository.deleteById(id);
        log.info("Bug with id [{}] deleted", id);
    }


    public void assignStoryDeveloper(Integer id, AssigneeRequest assigneeRequest) {
        log.debug("Assign developer [{}] for story with id [{}]", assigneeRequest.getId(), id);
        var storyEntity = storyRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.STORY_NOT_FOUND));
        var developerEntity = developerRepository.findById(assigneeRequest.getId())
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.DEVELOPER_NOT_FOUND));
        storyEntity.setDeveloper(developerEntity);
        storyRepository.save(storyEntity);
        log.info("Developer [{}] assigned to story with id [{}]", assigneeRequest.getId(), id);
    }

    public void assignBugDeveloper(Integer id, AssigneeRequest assigneeRequest) {
        log.debug("Assign developer [{}] for bug with id [{}]", assigneeRequest.getId(), id);
        var bugEntity = bugRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.BUG_NOT_FOUND));
        var developerEntity = developerRepository.findById(assigneeRequest.getId())
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.DEVELOPER_NOT_FOUND));
        bugEntity.setDeveloper(developerEntity);
        bugRepository.save(bugEntity);
        log.info("Developer [{}] assigned to bug with id [{}]", assigneeRequest.getId(), id);
    }

    private StoryEntity buildStoryEntity(StoryCreateRequest request) {
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setTitle(request.getTitle());
        storyEntity.setDescription(request.getDescription());
        storyEntity.setStoryPoint(request.getStoryPoint());
        storyEntity.setCreationDate(LocalDateTime.now());
        storyEntity.setStatus(StoryStatus.NEW);
        return storyEntity;
    }

    private StoryEntity buildStoryEntity(StoryEntity storyEntity, StoryUpdateRequest request) {
        storyEntity.setTitle(request.getTitle());
        storyEntity.setDescription(request.getDescription());
        storyEntity.setStoryPoint(request.getStoryPoint());
        storyEntity.setStatus(StoryStatus.fromValue(request.getStatus()));
        return storyEntity;
    }

    private BugEntity buildBugEntity(BugCreateRequest request) {
        BugEntity bugEntity = new BugEntity();
        bugEntity.setTitle(request.getTitle());
        bugEntity.setDescription(request.getDescription());
        bugEntity.setPriority(Priority.fromValue(request.getPriority()));
        bugEntity.setCreationDate(LocalDateTime.now());
        bugEntity.setStatus(BugStatus.NEW);
        return bugEntity;
    }

    private BugEntity buildBugEntity(BugEntity bugEntity, BugUpdateRequest request) {
        bugEntity.setTitle(request.getTitle());
        bugEntity.setDescription(request.getDescription());
        bugEntity.setPriority(Priority.fromValue(request.getPriority()));
        bugEntity.setStatus(BugStatus.fromValue(request.getStatus()));
        return bugEntity;
    }
}
