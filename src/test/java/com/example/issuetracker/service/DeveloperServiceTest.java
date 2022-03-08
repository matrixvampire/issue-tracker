package com.example.issuetracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.issuetracker.controller.request.DeveloperCreateUpdateRequest;
import com.example.issuetracker.controller.response.DeveloperListResponse;
import com.example.issuetracker.controller.response.DeveloperResponse;
import com.example.issuetracker.domain.ResponseMessageEnum;
import com.example.issuetracker.exception.BadRequestException;
import com.example.issuetracker.repository.DeveloperRepository;
import com.example.issuetracker.repository.entity.DeveloperEntity;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;
    @InjectMocks
    private DeveloperService developerService;

    @Test
    void test__createDeveloper__shouldSuccess() {
        when(developerRepository.save(any(DeveloperEntity.class))).thenAnswer(invocation -> {
            DeveloperEntity developerEntity = invocation.getArgument(0, DeveloperEntity.class);
            developerEntity.setId(1);
            return developerEntity;
        });

        DeveloperResponse developerResponse = developerService.createDeveloper(buildDeveloperCreateUpdateRequest());

        assertThat(developerResponse).isNotNull();
        assertThat(developerResponse.getId()).isEqualTo(1);
        assertThat(developerResponse.getName()).isEqualTo("name");

        verify(developerRepository).save(any(DeveloperEntity.class));
    }

    @Test
    void test__getDeveloper__shouldSuccess() {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(buildDeveloperEntity()));

        DeveloperResponse developerResponse = developerService.getDeveloper(1);

        assertThat(developerResponse).isNotNull();
        assertThat(developerResponse.getId()).isEqualTo(1);
        assertThat(developerResponse.getName()).isEqualTo("name");
    }

    @Test
    void test__getDeveloper__whenDeveloperNotFound__shouldFail() {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> developerService.getDeveloper(1));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.DEVELOPER_NOT_FOUND);
    }

    @Test
    void test__updateDeveloper__shouldSuccess() {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.of(buildDeveloperEntity()));

        when(developerRepository.save(any(DeveloperEntity.class))).thenAnswer(invocation -> invocation.getArgument(0, DeveloperEntity.class));

        DeveloperResponse developerResponse = developerService.updateDeveloper(1, buildDeveloperCreateUpdateRequest());

        assertThat(developerResponse).isNotNull();
        assertThat(developerResponse.getId()).isEqualTo(1);
        assertThat(developerResponse.getName()).isEqualTo("name");

        verify(developerRepository).save(any(DeveloperEntity.class));
    }

    @Test
    void test__updateDeveloper__whenDeveloperNotFound__shouldFail() {
        when(developerRepository.findById(anyInt())).thenReturn(Optional.empty());

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
            () -> developerService.updateDeveloper(1, buildDeveloperCreateUpdateRequest()));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.DEVELOPER_NOT_FOUND);

        verify(developerRepository, never()).save(any(DeveloperEntity.class));
    }

    @Test
    void test__deleteDeveloper__shouldSuccess() {
        when(developerRepository.existsById(anyInt())).thenReturn(Boolean.TRUE);

        developerService.deleteDeveloper(1);

        verify(developerRepository).deleteById(1);
    }

    @Test
    void test__deleteDeveloper__whenDeveloperNotFound__shouldFail() {
        when(developerRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> developerService.deleteDeveloper(1));
        assertThat(badRequestException).isNotNull();
        assertThat(badRequestException.getResponseMessage()).isEqualTo(ResponseMessageEnum.DEVELOPER_NOT_FOUND);

        verify(developerRepository, never()).deleteById(1);
    }

    @Test
    void test__getAllDeveloper__shouldSuccess() {
        when(developerRepository.findAll()).thenReturn(Collections.singletonList(buildDeveloperEntity()));

        DeveloperListResponse developerListResponse = developerService.getAllDeveloper();

        assertThat(developerListResponse).isNotNull();
        assertThat(developerListResponse.getDevelopers()).hasSize(1);
        assertThat(developerListResponse.getDevelopers().get(0).getId()).isEqualTo(1);
        assertThat(developerListResponse.getDevelopers().get(0).getName()).isEqualTo("name");
    }

    @Test
    void test__getAllDeveloper__withNoDevelopers__shouldSuccessAndEmptyList() {
        when(developerRepository.findAll()).thenReturn(Collections.emptyList());

        DeveloperListResponse developerListResponse = developerService.getAllDeveloper();

        assertThat(developerListResponse).isNotNull();
        assertThat(developerListResponse.getDevelopers()).isEmpty();
    }

    private DeveloperCreateUpdateRequest buildDeveloperCreateUpdateRequest() {
        return new DeveloperCreateUpdateRequest("name");
    }

    private DeveloperEntity buildDeveloperEntity() {
        DeveloperEntity developerEntity = new DeveloperEntity();
        developerEntity.setId(1);
        developerEntity.setName("name");
        return developerEntity;
    }
}