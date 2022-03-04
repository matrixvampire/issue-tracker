package com.example.issuetracker.repository;

import com.example.issuetracker.domain.StoryStatus;
import com.example.issuetracker.repository.entity.StoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<StoryEntity, Integer> {

    public List<StoryEntity> findByStatusIn(List<StoryStatus> statusList);
}
