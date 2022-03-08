package com.example.issuetracker.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
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

import com.example.issuetracker.controller.request.DeveloperCreateUpdateRequest;
import com.example.issuetracker.controller.response.DeveloperListResponse;
import com.example.issuetracker.controller.response.DeveloperResponse;
import com.example.issuetracker.domain.ResponseMessageEnum;
import com.example.issuetracker.exception.BadRequestException;
import com.example.issuetracker.service.DeveloperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

    private static final String CREATE_DEVELOPER = "/developers";
    private static final String GET_DEVELOPER = "/developers/{id}";
    private static final String UPDATE_DEVELOPER = "/developers/{id}";
    private static final String DELETE_DEVELOPER = "/developers/{id}";
    private static final String GET_ALL_DEVELOPER = "/developers";

    private static final Integer ID = 1;
    private static final String NAME = "name";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeveloperService developerService;

    @Test
    void test__createDeveloper__shouldSuccess() throws Exception {
        DeveloperResponse response = new DeveloperResponse(ID, NAME);
        when(developerService.createDeveloper(any(DeveloperCreateUpdateRequest.class))).thenReturn(response);

        DeveloperCreateUpdateRequest request = new DeveloperCreateUpdateRequest();
        request.setName(NAME);

        mockMvc.perform(post(CREATE_DEVELOPER)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(ID)),
                jsonPath("$.data.name", is(NAME)));

        verify(developerService).createDeveloper(any(DeveloperCreateUpdateRequest.class));
    }

    @Test
    void test__getDeveloper__shouldSuccess() throws Exception {
        DeveloperResponse response = new DeveloperResponse(ID, NAME);
        when(developerService.getDeveloper(anyInt())).thenReturn(response);

        mockMvc.perform(get(GET_DEVELOPER, ID))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(ID)),
                jsonPath("$.data.name", is(NAME)));

        verify(developerService).getDeveloper(ID);
    }

    @Test
    void test__updateDeveloper__shouldSuccess() throws Exception {
        DeveloperResponse response = new DeveloperResponse(ID, NAME);
        when(developerService.updateDeveloper(anyInt(), any(DeveloperCreateUpdateRequest.class))).thenReturn(response);

        DeveloperCreateUpdateRequest request = new DeveloperCreateUpdateRequest();
        request.setName(NAME);

        mockMvc.perform(put(UPDATE_DEVELOPER, ID)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.id", is(ID)),
                jsonPath("$.data.name", is(NAME)));

        verify(developerService).updateDeveloper(eq(ID), any(DeveloperCreateUpdateRequest.class));
    }

    @Test
    void test__deleteDeveloper__shouldSuccess() throws Exception {
        mockMvc.perform(delete(DELETE_DEVELOPER, ID))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data", nullValue()));

        verify(developerService).deleteDeveloper(ID);
    }

    @Test
    void test__getAllDeveloper__shouldSuccess() throws Exception {
        DeveloperListResponse response = new DeveloperListResponse();
        response.setDevelopers(Collections.emptyList());
        when(developerService.getAllDeveloper()).thenReturn(response);

        mockMvc.perform(get(GET_ALL_DEVELOPER)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.developers", empty()));

        verify(developerService).getAllDeveloper();
    }

    @Test
    void test__getDeveloper__onBadRequest__shouldFail() throws Exception {
        when(developerService.getDeveloper(anyInt())).thenThrow(new BadRequestException(ResponseMessageEnum.DEVELOPER_NOT_FOUND));

        mockMvc.perform(get(GET_DEVELOPER, ID))
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message", is(ResponseMessageEnum.DEVELOPER_NOT_FOUND.getMessage())),
                jsonPath("$.data", nullValue()));

        verify(developerService).getDeveloper(ID);
    }
}