package com.example.issuetracker.service;

import static com.example.issuetracker.util.ResponseBuilderUtil.buildIssueResponse;
import static com.example.issuetracker.util.ResponseBuilderUtil.buildPlanResponse;

import com.example.issuetracker.config.ApplicationConfiguration;
import com.example.issuetracker.controller.response.IssueResponse;
import com.example.issuetracker.controller.response.PlanListResponse;
import com.example.issuetracker.controller.response.PlanResponse;
import com.example.issuetracker.domain.StoryStatus;
import com.example.issuetracker.repository.DeveloperRepository;
import com.example.issuetracker.repository.StoryRepository;
import com.example.issuetracker.repository.entity.StoryEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlanService {

    private final ApplicationConfiguration applicationConfiguration;
    private final DeveloperRepository developerRepository;
    private final StoryRepository storyRepository;

    public PlanListResponse getPlan() {
        log.info("Calculating plan");
        PlanListResponse response = new PlanListResponse();
        long numberOfDevelopers = developerRepository.count();
        log.debug("Number of developers found [{}]", numberOfDevelopers);
        if (numberOfDevelopers > 0) {
            long maximumStoryPoints = numberOfDevelopers * applicationConfiguration.getAverageStoryPoint();
            log.debug("Maximum story points [{}] per week", maximumStoryPoints);
            List<StoryEntity> storyList = storyRepository.findByStatusIn(List.of(StoryStatus.NEW, StoryStatus.ESTIMATED));
            log.debug("Number of new and estimated stories [{}]", storyList.size());
            if (!CollectionUtils.isEmpty(storyList)) {
                MultiValueMap<Integer, IssueResponse> weekPlan = new LinkedMultiValueMap<>();
                int week = 1;
                long sumOfStoryPoints = 0;
                for (StoryEntity storyEntity : storyList) {
                    sumOfStoryPoints += storyEntity.getStoryPoint();
                    if (sumOfStoryPoints >= maximumStoryPoints) {
                        week += 1;
                        sumOfStoryPoints = storyEntity.getStoryPoint();
                    }
                    weekPlan.add(week, buildIssueResponse(storyEntity));
                }
                List<PlanResponse> planResponseList = weekPlan.entrySet()
                    .stream()
                    .map(entry -> buildPlanResponse(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
                response.setPlans(planResponseList);
                log.info("Number of weeks [{}]", week);
            }
        }
        log.info("Plan calculated");
        return response;
    }
}
