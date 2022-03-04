package com.example.issuetracker.repository;

import com.example.issuetracker.repository.entity.BugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<BugEntity, Integer> {

}
