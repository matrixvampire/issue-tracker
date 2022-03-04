package com.example.issuetracker.controller;

import com.example.issuetracker.controller.request.DeveloperCreateUpdateRequest;
import com.example.issuetracker.controller.response.DeveloperListResponse;
import com.example.issuetracker.controller.response.DeveloperResponse;
import com.example.issuetracker.domain.Response;
import com.example.issuetracker.domain.ResponseFactory;
import com.example.issuetracker.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    @PostMapping(value = "/developers")
    public ResponseEntity<Response<DeveloperResponse>> createDeveloper(@RequestBody DeveloperCreateUpdateRequest developerCreateUpdateRequest) {
        return ResponseFactory.success(developerService.createDeveloper(developerCreateUpdateRequest), DeveloperResponse.class);
    }

    @GetMapping(value = "/developers/{id}")
    public ResponseEntity<Response<DeveloperResponse>> getDeveloper(@PathVariable("id") Integer id) {
        return ResponseFactory.success(developerService.getDeveloper(id), DeveloperResponse.class);
    }

    @PutMapping(value = "/developers/{id}")
    public ResponseEntity<Response<DeveloperResponse>> updateDeveloper(@PathVariable("id") Integer id,
        @RequestBody DeveloperCreateUpdateRequest developerCreateUpdateRequest) {
        return ResponseFactory.success(developerService.updateDeveloper(id, developerCreateUpdateRequest), DeveloperResponse.class);
    }

    @DeleteMapping(value = "/developers/{id}")
    public ResponseEntity<Response<Object>> deleteDeveloper(@PathVariable("id") Integer id) {
        developerService.deleteDeveloper(id);
        return ResponseFactory.success();
    }

    @GetMapping(value = "/developers")
    public ResponseEntity<Response<DeveloperListResponse>> getAllDeveloper() {
        return ResponseFactory.success(developerService.getAllDeveloper(), DeveloperListResponse.class);
    }
}
