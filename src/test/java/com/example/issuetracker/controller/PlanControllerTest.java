package com.example.issuetracker.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.issuetracker.controller.response.PlanListResponse;
import com.example.issuetracker.domain.ResponseMessageEnum;
import com.example.issuetracker.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlanController.class)
class PlanControllerTest {

    private static final String GET_PLAN = "/plan";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanService planService;

    @Test
    void test__getPlan__shouldSuccess() throws Exception {
        PlanListResponse planListResponse = new PlanListResponse();
        when(planService.getPlan()).thenReturn(planListResponse);

        mockMvc.perform(get(GET_PLAN))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.message", is(ResponseMessageEnum.SUCCESS.getMessage())),
                jsonPath("$.data.plans", nullValue()));
    }
}