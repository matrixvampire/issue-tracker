package com.example.issuetracker.repository;

import com.example.issuetracker.repository.entity.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Integer> {

}
