package com.example.issuetracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.issuetracker.repository.entity.DeveloperEntity;
import com.example.issuetracker.repository.entity.StoryEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private StoryRepository storyRepository;
    @Mock
    private BugRepository bugRepository;
    @Mock
    private DeveloperRepository developerRepository;
    @InjectMocks
    private IssueService issueService;

    @Test
    void test__createStory__shouldSuccess() {
        when(storyRepository.save(any(StoryEntity.class))).thenAnswer(invocation -> {
            StoryEntity storyEntity = invocation.getArgument(0, StoryEntity.class);
            storyEntity.setId(1);
            return storyEntity;
        });

        StoryResponse storyResponse = issueService.createStory(buildStoryCreateRequest());

        assertThat(storyResponse).isNotNull();
        assertThat(storyResponse.getId()).isEqualTo(1);
        assertThat(storyResponse.getTitle()).isEqualTo("title");
        assertThat(storyResponse.getDescription()).isEqualTo("description");
        assertThat(storyResponse.getStoryPoint()).isEqualTo(2);
        assertThat(storyResponse.getStatus()).isEqualTo(StoryStatus.NEW.name());
        assertThat(storyResponse.getCreationDate()).isNotNull();

        verify(storyRepository).save(any(StoryEntity.class));
    }

    @Test
    void test__getStory__shouldSuccess() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.of(buildStoryEntity()));

        StoryResponse storyResponse = issueService.getStory(1);

        assertThat(storyResponse).isNotNull();
        assertThat(storyResponse.getId()).isEqualTo(1);
        assertThat(storyResponse.getTitle()).isEqualTo("title");
        assertThat(storyResponse.getDescription()).isEqualTo("description");
        assertThat(storyResponse.getStoryPoint()).isEqualTo(2);
        assertThat(storyResponse.getStatus()).isEqualTo(StoryStatus.NEW.name());
        assertThat(storyResponse.getCreationDate()).isNotNull();
    }

    @Test
    void test__getStory__withAssignDeveloper__shouldSuccess() {
        StoryEntity storyEntity = buildStoryEntity();
        storyEntity.setDeveloper(buildDeveloperEntity());
        when(storyRepository.findById(anyInt())).thenReturn(Optional.of(storyEntity));

        StoryResponse storyResponse = issueService.getStory(1);

        assertThat(storyResponse).isNotNull();
        assertThat(storyResponse.getId()).isEqualTo(1);
        assertThat(storyResponse.getTitle()).isEqualTo("title");
        assertThat(storyResponse.getDescription()).isEqualTo("description");
        assertThat(storyResponse.getStoryPoint()).isEqualTo(2);
        assertThat(storyResponse.getStatus()).isEqualTo(StoryStatus.NEW.name());
        assertThat(storyResponse.getCreationDate()).isNotNull();
        assertThat(storyResponse.getDeveloper()).isNotNull();
        assertThat(storyResponse.getDeveloper().getId()).isEqualTo(1);
        assertThat(storyResponse.getDeveloper().getName()).isEqualTo("developer");
    }

    @Test
    void test__getStory__whenStoryNotFound__shouldFail() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.getStory(1));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.STORY_NOT_FOUND);
    }

    @Test
    void test__updateStory__shouldSuccess() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.of(buildStoryEntity()));

        when(storyRepository.save(any(StoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0, StoryEntity.class));

        StoryResponse storyResponse = issueService.updateStory(1, buildStoryUpdateRequest());

        assertThat(storyResponse).isNotNull();
        assertThat(storyResponse.getId()).isEqualTo(1);
        assertThat(storyResponse.getTitle()).isEqualTo("title updated");
        assertThat(storyResponse.getDescription()).isEqualTo("description updated");
        assertThat(storyResponse.getStoryPoint()).isEqualTo(3);
        assertThat(storyResponse.getStatus()).isEqualTo(StoryStatus.ESTIMATED.name());
        assertThat(storyResponse.getCreationDate()).isNotNull();

        verify(storyRepository).save(any(StoryEntity.class));
    }

    @Test
    void test__updateStory__whenStoryNotFound__shouldFail() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.updateStory(1, buildStoryUpdateRequest()));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.STORY_NOT_FOUND);

        verify(storyRepository, never()).save(any(StoryEntity.class));
    }

    @Test
    void test__deleteStory__shouldSuccess() {
        when(storyRepository.existsById(anyInt())).thenReturn(Boolean.TRUE);

        issueService.deleteStory(1);

        verify(storyRepository).deleteById(1);
    }

    @Test
    void test__deleteStory__whenBugNotFound__shouldFail() {
        when(storyRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.deleteStory(1));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.STORY_NOT_FOUND);

        verify(storyRepository, never()).deleteById(1);
    }

    @Test
    void test__createBug__shouldSuccess() {
        when(bugRepository.save(any(BugEntity.class))).thenAnswer(invocation -> {
            BugEntity bugEntity = invocation.getArgument(0, BugEntity.class);
            bugEntity.setId(1);
            return bugEntity;
        });

        BugResponse bugResponse = issueService.createBug(buildBugCreateRequest());

        assertThat(bugResponse).isNotNull();
        assertThat(bugResponse.getId()).isEqualTo(1);
        assertThat(bugResponse.getTitle()).isEqualTo("title");
        assertThat(bugResponse.getDescription()).isEqualTo("description");
        assertThat(bugResponse.getPriority()).isEqualTo(Priority.CRITICAL.name());
        assertThat(bugResponse.getStatus()).isEqualTo(StoryStatus.NEW.name());
        assertThat(bugResponse.getCreationDate()).isNotNull();

        verify(bugRepository).save(any(BugEntity.class));
    }

    @Test
    void test__getBug__shouldSuccess() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.of(buildBugEntity()));

        BugResponse bugResponse = issueService.getBug(1);

        assertThat(bugResponse).isNotNull();
        assertThat(bugResponse.getId()).isEqualTo(1);
        assertThat(bugResponse.getTitle()).isEqualTo("title");
        assertThat(bugResponse.getDescription()).isEqualTo("description");
        assertThat(bugResponse.getPriority()).isEqualTo(Priority.CRITICAL.name());
        assertThat(bugResponse.getStatus()).isEqualTo(StoryStatus.NEW.name());
        assertThat(bugResponse.getCreationDate()).isNotNull();
    }

    @Test
    void test__getBug__withAssignDeveloper__shouldSuccess() {
        BugEntity bugEntity = buildBugEntity();
        bugEntity.setDeveloper(buildDeveloperEntity());
        when(bugRepository.findById(anyInt())).thenReturn(Optional.of(bugEntity));

        BugResponse bugResponse = issueService.getBug(1);

        assertThat(bugResponse).isNotNull();
        assertThat(bugResponse.getId()).isEqualTo(1);
        assertThat(bugResponse.getTitle()).isEqualTo("title");
        assertThat(bugResponse.getDescription()).isEqualTo("description");
        assertThat(bugResponse.getPriority()).isEqualTo(Priority.CRITICAL.name());
        assertThat(bugResponse.getStatus()).isEqualTo(StoryStatus.NEW.name());
        assertThat(bugResponse.getCreationDate()).isNotNull();
        assertThat(bugResponse.getDeveloper()).isNotNull();
        assertThat(bugResponse.getDeveloper().getId()).isEqualTo(1);
        assertThat(bugResponse.getDeveloper().getName()).isEqualTo("developer");
    }

    @Test
    void test__getBug__whenStoryNotFound__shouldFail() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.getBug(1));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.BUG_NOT_FOUND);
    }

    @Test
    void test__updateBug__shouldSuccess() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.of(buildBugEntity()));

        when(bugRepository.save(any(BugEntity.class))).thenAnswer(invocation -> invocation.getArgument(0, BugEntity.class));

        BugResponse bugResponse = issueService.updateBug(1, buildBugUpdateRequest());

        assertThat(bugResponse).isNotNull();
        assertThat(bugResponse.getId()).isEqualTo(1);
        assertThat(bugResponse.getTitle()).isEqualTo("title updated");
        assertThat(bugResponse.getDescription()).isEqualTo("description updated");
        assertThat(bugResponse.getPriority()).isEqualTo(Priority.MAJOR.name());
        assertThat(bugResponse.getStatus()).isEqualTo(BugStatus.VERIFIED.name());
        assertThat(bugResponse.getCreationDate()).isNotNull();

        verify(bugRepository).save(any(BugEntity.class));
    }

    @Test
    void test__updateBug__whenStoryNotFound__shouldFail() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.updateBug(1, buildBugUpdateRequest()));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.BUG_NOT_FOUND);

        verify(bugRepository, never()).save(any(BugEntity.class));
    }

    @Test
    void test__deleteBug__shouldSuccess() {
        when(bugRepository.existsById(anyInt())).thenReturn(Boolean.TRUE);

        issueService.deleteBug(1);

        verify(bugRepository).deleteById(1);
    }

    @Test
    void test__deleteBug__whenBugNotFound__shouldFail() {
        when(bugRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.deleteBug(1));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.BUG_NOT_FOUND);

        verify(bugRepository, never()).deleteById(1);
    }

    @Test
    void test__assignStoryDeveloper__shouldSuccess() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.of(buildStoryEntity()));
        DeveloperEntity developerEntity = buildDeveloperEntity();
        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(developerEntity));

        issueService.assignStoryDeveloper(1, new AssigneeRequest(1));

        ArgumentCaptor<StoryEntity> argumentCaptor = ArgumentCaptor.forClass(StoryEntity.class);
        verify(storyRepository).save(argumentCaptor.capture());

        StoryEntity capturedStoryEntity = argumentCaptor.getValue();
        assertThat(capturedStoryEntity.getDeveloper()).isEqualTo(developerEntity);
    }

    @Test
    void test__assignStoryDeveloper__whenStoryNotFound__shouldFail() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.assignStoryDeveloper(1, new AssigneeRequest(1)));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.STORY_NOT_FOUND);

        verify(storyRepository, never()).save(any(StoryEntity.class));
    }

    @Test
    void test__assignStoryDeveloper__whenDeveloperNotFound__shouldFail() {
        when(storyRepository.findById(anyInt())).thenReturn(Optional.of(buildStoryEntity()));
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.assignStoryDeveloper(1, new AssigneeRequest(1)));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.DEVELOPER_NOT_FOUND);

        verify(storyRepository, never()).save(any(StoryEntity.class));
    }

    @Test
    void test__assignBugDeveloper__shouldSuccess() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.of(buildBugEntity()));
        DeveloperEntity developerEntity = buildDeveloperEntity();
        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(developerEntity));

        issueService.assignBugDeveloper(1, new AssigneeRequest(1));

        ArgumentCaptor<BugEntity> argumentCaptor = ArgumentCaptor.forClass(BugEntity.class);
        verify(bugRepository).save(argumentCaptor.capture());

        BugEntity capturedBugEntity = argumentCaptor.getValue();
        assertThat(capturedBugEntity.getDeveloper()).isEqualTo(developerEntity);
    }

    @Test
    void test__assignBugDeveloper__whenStoryNotFound__shouldFail() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.assignBugDeveloper(1, new AssigneeRequest(1)));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.BUG_NOT_FOUND);

        verify(bugRepository, never()).save(any(BugEntity.class));
    }

    @Test
    void test__assignBugDeveloper__whenDeveloperNotFound__shouldFail() {
        when(bugRepository.findById(anyInt())).thenReturn(Optional.of(buildBugEntity()));
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> issueService.assignBugDeveloper(1, new AssigneeRequest(1)));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.DEVELOPER_NOT_FOUND);

        verify(bugRepository, never()).save(any(BugEntity.class));
    }

    private StoryCreateRequest buildStoryCreateRequest() {
        StoryCreateRequest storyCreateRequest = new StoryCreateRequest();
        storyCreateRequest.setTitle("title");
        storyCreateRequest.setDescription("description");
        storyCreateRequest.setStoryPoint(2);
        return storyCreateRequest;
    }

    private StoryUpdateRequest buildStoryUpdateRequest() {
        StoryUpdateRequest storyUpdateRequest = new StoryUpdateRequest();
        storyUpdateRequest.setTitle("title updated");
        storyUpdateRequest.setDescription("description updated");
        storyUpdateRequest.setStoryPoint(3);
        storyUpdateRequest.setStatus("Estimated");
        return storyUpdateRequest;
    }

    private StoryEntity buildStoryEntity() {
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setId(1);
        storyEntity.setTitle("title");
        storyEntity.setDescription("description");
        storyEntity.setStoryPoint(2);
        storyEntity.setCreationDate(LocalDateTime.now());
        storyEntity.setStatus(StoryStatus.NEW);
        return storyEntity;
    }

    private BugCreateRequest buildBugCreateRequest() {
        BugCreateRequest bugCreateRequest = new BugCreateRequest();
        bugCreateRequest.setTitle("title");
        bugCreateRequest.setDescription("description");
        bugCreateRequest.setPriority("Critical");
        return bugCreateRequest;
    }

    private BugUpdateRequest buildBugUpdateRequest() {
        BugUpdateRequest bugUpdateRequest = new BugUpdateRequest();
        bugUpdateRequest.setTitle("title updated");
        bugUpdateRequest.setDescription("description updated");
        bugUpdateRequest.setPriority("Major");
        bugUpdateRequest.setStatus("Verified");
        return bugUpdateRequest;
    }

    private BugEntity buildBugEntity() {
        BugEntity bugEntity = new BugEntity();
        bugEntity.setId(1);
        bugEntity.setTitle("title");
        bugEntity.setDescription("description");
        bugEntity.setPriority(Priority.CRITICAL);
        bugEntity.setCreationDate(LocalDateTime.now());
        bugEntity.setStatus(BugStatus.NEW);
        return bugEntity;
    }

    private DeveloperEntity buildDeveloperEntity() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(1);
        developerEntity.setName("developer");
        return developerEntity;
    }
}