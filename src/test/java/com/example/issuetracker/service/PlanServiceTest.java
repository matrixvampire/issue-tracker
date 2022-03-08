package com.example.issuetracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.issuetracker.config.ApplicationConfiguration;
import com.example.issuetracker.controller.response.IssueResponse;
import com.example.issuetracker.controller.response.PlanListResponse;
import com.example.issuetracker.domain.IssueType;
import com.example.issuetracker.domain.StoryStatus;
import com.example.issuetracker.repository.DeveloperRepository;
import com.example.issuetracker.repository.StoryRepository;
import com.example.issuetracker.repository.entity.DeveloperEntity;
import com.example.issuetracker.repository.entity.StoryEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private ApplicationConfiguration applicationConfiguration;
    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private StoryRepository storyRepository;
    @InjectMocks
    private PlanService planService;

    @Test
    void test__getPlan__shouldSuccess() {
        when(applicationConfiguration.getAverageStoryPoint()).thenReturn(10);
        when(developerRepository.count()).thenReturn(2L);
        when(storyRepository.findByStatusIn(anyList())).thenReturn(buildStoryEntity());

        PlanListResponse planListResponse = planService.getPlan();

        assertThat(planListResponse).isNotNull();
        assertThat(planListResponse.getPlans()).hasSize(2);

        Integer sumStoryPoints0 = planListResponse.getPlans().get(0).getIssues().stream().mapToInt(IssueResponse::getStoryPoint).sum();
        assertThat(sumStoryPoints0).isLessThanOrEqualTo(20);
        Integer sumStoryPoints1 = planListResponse.getPlans().get(1).getIssues().stream().mapToInt(IssueResponse::getStoryPoint).sum();
        assertThat(sumStoryPoints1).isLessThanOrEqualTo(20);

        verify(developerRepository).count();
        verify(storyRepository).findByStatusIn(anyList());
    }

    @Test
    void test__getPlan__withNoDevelopers__shouldSuccessWithoutPlan() {
        when(developerRepository.count()).thenReturn(0L);

        PlanListResponse planListResponse = planService.getPlan();

        assertThat(planListResponse).isNotNull();
        assertThat(planListResponse.getPlans()).isNullOrEmpty();

        verify(developerRepository).count();
        verify(storyRepository, never()).findByStatusIn(anyList());
    }

    @Test
    void test__getPlan__withNoStories__shouldSuccessWithoutPlan() {
        when(applicationConfiguration.getAverageStoryPoint()).thenReturn(10);
        when(developerRepository.count()).thenReturn(2L);
        when(storyRepository.findByStatusIn(anyList())).thenReturn(Collections.emptyList());

        PlanListResponse planListResponse = planService.getPlan();

        assertThat(planListResponse).isNotNull();
        assertThat(planListResponse.getPlans()).isNullOrEmpty();

        verify(developerRepository).count();
        verify(storyRepository).findByStatusIn(anyList());
    }

    private List<StoryEntity> buildStoryEntity() {
        List<StoryEntity> storyList = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            StoryEntity storyEntity = new StoryEntity();
            storyEntity.setId(i);
            storyEntity.setType(IssueType.STORY);
            storyEntity.setStoryPoint(i % 2 == 0 ? 2 : i % 3 == 0 ? 3 : 1);
            storyEntity.setStatus(i % 2 == 0 ? StoryStatus.NEW : StoryStatus.ESTIMATED);
            storyEntity.setDeveloper(i % 3 == 0 ? buildDeveloperEntity() : null);
            storyList.add(storyEntity);
        }
        return storyList;
    }

    private DeveloperEntity buildDeveloperEntity() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(1);
        return developerEntity;
    }
}