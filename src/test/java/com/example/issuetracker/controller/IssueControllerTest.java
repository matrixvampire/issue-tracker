package com.example.issuetracker.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.example.issuetracker.service.IssueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(IssueController.class)
class IssueControllerTest {

    private static final String CREATE_STORY = "/stories";
    private static final String GET_STORY = "/stories/{id}";
    private static final String UPDATE_STORY = "/stories/{id}";
    private static final String DELETE_STORY = "/stories/{id}";
    private static final String ASSIGN_STORY_DEVELOPER = "/stories/{id}/developers";
    private static final String CREATE_BUG = "/bugs";
    private static final String GET_BUG = "/bugs/{id}";
    private static final String UPDATE_BUG = "/bugs/{id}";
    private static final String DELETE_BUG = "/bugs/{id}";
    private static final String ASSIGN_BUG_DEVELOPER = "/bugs/{id}/developers";

    private static final Integer ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    @Test
    void test__createStory__shouldSuccess() throws Exception {
        StoryResponse storyResponse = buildStoryResponse();
        when(issueService.createStory(any(StoryCreateRequest.class))).thenReturn(storyResponse);

        mockMvc.perform(post(CREATE_STORY)
                .content(objectMapper.writeValueAsString(buildStoryCreateRequest()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(storyResponse.getId())),
                jsonPath("$.data.title", is(storyResponse.getTitle())),
                jsonPath("$.data.description", is(storyResponse.getDescription())),
                jsonPath("$.data.story_point", is(storyResponse.getStoryPoint())),
                jsonPath("$.data.creation_date", notNullValue()),
                jsonPath("$.data.status", is(storyResponse.getStatus())),
                jsonPath("$.data.developer", nullValue()));

        verify(issueService).createStory(any(StoryCreateRequest.class));
    }

    @Test
    void test__getStory__shouldSuccess() throws Exception {
        StoryResponse storyResponse = buildStoryResponse();
        when(issueService.getStory(anyInt())).thenReturn(storyResponse);

        mockMvc.perform(get(GET_STORY, ID))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(storyResponse.getId())),
                jsonPath("$.data.title", is(storyResponse.getTitle())),
                jsonPath("$.data.description", is(storyResponse.getDescription())),
                jsonPath("$.data.story_point", is(storyResponse.getStoryPoint())),
                jsonPath("$.data.creation_date", notNullValue()),
                jsonPath("$.data.status", is(storyResponse.getStatus())),
                jsonPath("$.data.developer", nullValue()));

        verify(issueService).getStory(ID);
    }

    @Test
    void test__updateStory__shouldSuccess() throws Exception {
        StoryResponse storyResponse = buildStoryResponse();
        when(issueService.updateStory(anyInt(), any(StoryUpdateRequest.class))).thenReturn(storyResponse);

        mockMvc.perform(put(UPDATE_STORY, ID)
                .content(objectMapper.writeValueAsString(buildStoryUpdateRequest()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(storyResponse.getId())),
                jsonPath("$.data.title", is(storyResponse.getTitle())),
                jsonPath("$.data.description", is(storyResponse.getDescription())),
                jsonPath("$.data.story_point", is(storyResponse.getStoryPoint())),
                jsonPath("$.data.creation_date", notNullValue()),
                jsonPath("$.data.status", is(storyResponse.getStatus())),
                jsonPath("$.data.developer", nullValue()));

        verify(issueService).updateStory(eq(ID), any(StoryUpdateRequest.class));
    }

    @Test
    void test__deleteStory__shouldSuccess() throws Exception {
        mockMvc.perform(delete(DELETE_STORY, ID))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data", nullValue()));

        verify(issueService).deleteStory(ID);
    }

    @Test
    void test__assignStoryDeveloper__shouldSuccess() throws Exception {
        mockMvc.perform(post(ASSIGN_STORY_DEVELOPER, ID)
                .content(objectMapper.writeValueAsString(buildAssigneeRequest()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data", nullValue()));

        verify(issueService).assignStoryDeveloper(eq(ID), any(AssigneeRequest.class));
    }

    @Test
    void test__createBug__shouldSuccess() throws Exception {
        BugResponse bugResponse = buildBugResponse();
        when(issueService.createBug(any(BugCreateRequest.class))).thenReturn(bugResponse);

        mockMvc.perform(post(CREATE_BUG)
                .content(objectMapper.writeValueAsString(buildBugCreateRequest()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(bugResponse.getId())),
                jsonPath("$.data.title", is(bugResponse.getTitle())),
                jsonPath("$.data.description", is(bugResponse.getDescription())),
                jsonPath("$.data.priority", is(bugResponse.getPriority())),
                jsonPath("$.data.creation_date", notNullValue()),
                jsonPath("$.data.status", is(bugResponse.getStatus())),
                jsonPath("$.data.developer", nullValue()));

        verify(issueService).createBug(any(BugCreateRequest.class));
    }

    @Test
    void test__getBug__shouldSuccess() throws Exception {
        BugResponse bugResponse = buildBugResponse();
        when(issueService.getBug(anyInt())).thenReturn(bugResponse);

        mockMvc.perform(get(GET_BUG, ID))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(bugResponse.getId())),
                jsonPath("$.data.title", is(bugResponse.getTitle())),
                jsonPath("$.data.description", is(bugResponse.getDescription())),
                jsonPath("$.data.priority", is(bugResponse.getPriority())),
                jsonPath("$.data.creation_date", notNullValue()),
                jsonPath("$.data.status", is(bugResponse.getStatus())),
                jsonPath("$.data.developer", nullValue()));

        verify(issueService).getBug(ID);
    }

    @Test
    void test__updateBug__shouldSuccess() throws Exception {
        BugResponse bugResponse = buildBugResponse();
        when(issueService.updateBug(anyInt(), any(BugUpdateRequest.class))).thenReturn(bugResponse);

        mockMvc.perform(put(UPDATE_BUG, ID)
                .content(objectMapper.writeValueAsString(buildBugUpdateRequest()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(bugResponse.getId())),
                jsonPath("$.data.title", is(bugResponse.getTitle())),
                jsonPath("$.data.description", is(bugResponse.getDescription())),
                jsonPath("$.data.priority", is(bugResponse.getPriority())),
                jsonPath("$.data.creation_date", notNullValue()),
                jsonPath("$.data.status", is(bugResponse.getStatus())),
                jsonPath("$.data.developer", nullValue()));

        verify(issueService).updateBug(eq(ID), any(BugUpdateRequest.class));
    }

    @Test
    void test__deleteBug__shouldSuccess() throws Exception {
        mockMvc.perform(delete(DELETE_BUG, ID))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data", nullValue()));

        verify(issueService).deleteBug(ID);
    }

    @Test
    void test__assignBugDeveloper__shouldSuccess() throws Exception {
        mockMvc.perform(post(ASSIGN_BUG_DEVELOPER, ID)
                .content(objectMapper.writeValueAsString(buildAssigneeRequest()))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data", nullValue()));

        verify(issueService).assignBugDeveloper(eq(ID), any(AssigneeRequest.class));
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

    private StoryResponse buildStoryResponse() {
        StoryResponse storyResponse = new StoryResponse();
        storyResponse.setId(ID);
        storyResponse.setTitle("title");
        storyResponse.setDescription("description");
        storyResponse.setStoryPoint(2);
        storyResponse.setCreationDate(LocalDateTime.now());
        storyResponse.setStatus(StoryStatus.NEW.name());
        return storyResponse;
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

    private BugResponse buildBugResponse() {
        BugResponse bugResponse = new BugResponse();
        bugResponse.setId(ID);
        bugResponse.setTitle("title");
        bugResponse.setDescription("description");
        bugResponse.setPriority(Priority.CRITICAL.name());
        bugResponse.setCreationDate(LocalDateTime.now());
        bugResponse.setStatus(BugStatus.NEW.name());
        return bugResponse;
    }

    private AssigneeRequest buildAssigneeRequest() {
        AssigneeRequest assigneeRequest = new AssigneeRequest();
        assigneeRequest.setId(ID);
        return assigneeRequest;
    }
}