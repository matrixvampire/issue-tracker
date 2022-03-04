package com.example.issuetracker.controller;

import com.example.issuetracker.controller.response.PlanListResponse;
import com.example.issuetracker.domain.Response;
import com.example.issuetracker.domain.ResponseFactory;
import com.example.issuetracker.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping(value = "/plan")
    public ResponseEntity<Response<PlanListResponse>> getPlan() {
        return ResponseFactory.success(planService.getPlan(), PlanListResponse.class);
    }
}
