package com.example.issuetracker.controller;

import com.example.issuetracker.controller.request.AssigneeRequest;
import com.example.issuetracker.controller.request.BugCreateRequest;
import com.example.issuetracker.controller.request.BugUpdateRequest;
import com.example.issuetracker.controller.request.StoryCreateRequest;
import com.example.issuetracker.controller.request.StoryUpdateRequest;
import com.example.issuetracker.controller.response.BugResponse;
import com.example.issuetracker.controller.response.StoryResponse;
import com.example.issuetracker.domain.Response;
import com.example.issuetracker.domain.ResponseFactory;
import com.example.issuetracker.service.IssueService;
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
public class IssueController {

    private final IssueService issueService;

    @PostMapping(value = "/stories")
    public ResponseEntity<Response<StoryResponse>> createStory(@RequestBody StoryCreateRequest storyCreateRequest) {
        return ResponseFactory.success(issueService.createStory(storyCreateRequest), StoryResponse.class);
    }

    @GetMapping(value = "/stories/{id}")
    public ResponseEntity<Response<StoryResponse>> getStory(@PathVariable("id") Integer id) {
        return ResponseFactory.success(issueService.getStory(id), StoryResponse.class);
    }

    @PutMapping(value = "/stories/{id}")
    public ResponseEntity<Response<StoryResponse>> updateStory(@PathVariable("id") Integer id, @RequestBody StoryUpdateRequest storyUpdateRequest) {
        return ResponseFactory.success(issueService.updateStory(id, storyUpdateRequest), StoryResponse.class);
    }

    @DeleteMapping(value = "/stories/{id}")
    public ResponseEntity<Response<Object>> deleteStory(@PathVariable("id") Integer id) {
        issueService.deleteStory(id);
        return ResponseFactory.success();
    }

    @PostMapping(value = "/stories/{id}/developers")
    public ResponseEntity<Response<Object>> assignStoryDeveloper(@PathVariable("id") Integer id, @RequestBody AssigneeRequest assigneeRequest) {
        issueService.assignStoryDeveloper(id, assigneeRequest);
        return ResponseFactory.success();
    }

    @PostMapping(value = "/bugs")
    public ResponseEntity<Response<BugResponse>> createBug(@RequestBody BugCreateRequest bugCreateRequest) {
        return ResponseFactory.success(issueService.createBug(bugCreateRequest), BugResponse.class);
    }

    @GetMapping(value = "/bugs/{id}")
    public ResponseEntity<Response<BugResponse>> getBug(@PathVariable("id") Integer id) {
        return ResponseFactory.success(issueService.getBug(id), BugResponse.class);
    }

    @PutMapping(value = "/bugs/{id}")
    public ResponseEntity<Response<BugResponse>> updateBug(@PathVariable("id") Integer id, @RequestBody BugUpdateRequest bugUpdateRequest) {
        return ResponseFactory.success(issueService.updateBug(id, bugUpdateRequest), BugResponse.class);
    }

    @DeleteMapping(value = "/bugs/{id}")
    public ResponseEntity<Response<Object>> deleteBug(@PathVariable("id") Integer id) {
        issueService.deleteBug(id);
        return ResponseFactory.success();
    }

    @PostMapping(value = "/bugs/{id}/developers")
    public ResponseEntity<Response<Object>> assignBugDeveloper(@PathVariable("id") Integer id, @RequestBody AssigneeRequest assigneeRequest) {
        issueService.assignBugDeveloper(id, assigneeRequest);
        return ResponseFactory.success();
    }
}
