package com.example.issuetracker.service;

import static com.example.issuetracker.util.ResponseBuilderUtil.buildDeveloperResponse;

import com.example.issuetracker.controller.request.DeveloperCreateUpdateRequest;
import com.example.issuetracker.controller.response.DeveloperListResponse;
import com.example.issuetracker.controller.response.DeveloperResponse;
import com.example.issuetracker.domain.ResponseMessageEnum;
import com.example.issuetracker.exception.BadRequestException;
import com.example.issuetracker.repository.DeveloperRepository;
import com.example.issuetracker.repository.entity.DeveloperEntity;
import com.example.issuetracker.util.ResponseBuilderUtil;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperResponse createDeveloper(DeveloperCreateUpdateRequest developerCreateUpdateRequest) {
        log.debug("Create developer with request [{}]", developerCreateUpdateRequest);
        var developerEntity = developerRepository.save(buildDeveloperEntity(developerCreateUpdateRequest));
        log.info("Developer created with id [{}]", developerEntity.getId());
        return buildDeveloperResponse(developerEntity);
    }

    public DeveloperResponse getDeveloper(Integer id) {
        log.debug("Get developer by id [{}]", id);
        var developerEntity = developerRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.DEVELOPER_NOT_FOUND));
        return buildDeveloperResponse(developerEntity);
    }

    public DeveloperResponse updateDeveloper(Integer id, DeveloperCreateUpdateRequest developerCreateUpdateRequest) {
        log.debug("Update developer by id [{}] with request [{}]", id, developerCreateUpdateRequest);
        var developerEntity = developerRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ResponseMessageEnum.DEVELOPER_NOT_FOUND));
        developerEntity = developerRepository.save(buildDeveloperEntity(developerEntity, developerCreateUpdateRequest));
        log.info("Developer with id [{}] updated", developerEntity.getId());
        return buildDeveloperResponse(developerEntity);
    }

    public void deleteDeveloper(Integer id) {
        log.debug("Delete developer by id [{}]", id);
        boolean exists = developerRepository.existsById(id);
        if (!exists) {
            throw new BadRequestException(ResponseMessageEnum.DEVELOPER_NOT_FOUND);
        }
        developerRepository.deleteById(id);
        log.info("Developer with id [{}] deleted", id);
    }

    public DeveloperListResponse getAllDeveloper() {
        log.debug("Get all developers");
        var developerResponseList = developerRepository
            .findAll()
            .stream()
            .map(ResponseBuilderUtil::buildDeveloperResponse)
            .collect(Collectors.toList());
        log.info("Developer fetched. Count [{}]", developerResponseList.size());
        return new DeveloperListResponse(developerResponseList);
    }

    private DeveloperEntity buildDeveloperEntity(DeveloperCreateUpdateRequest developerCreateUpdateRequest) {
        return buildDeveloperEntity(new DeveloperEntity(), developerCreateUpdateRequest);
    }

    private DeveloperEntity buildDeveloperEntity(DeveloperEntity developerEntity, DeveloperCreateUpdateRequest developerCreateUpdateRequest) {
        developerEntity.setName(developerCreateUpdateRequest.getName());
        return developerEntity;
    }
}
