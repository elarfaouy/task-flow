package com.youcode.taskflow.repository;

import com.youcode.taskflow.domain.entity.JetonUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JetonUsageRepository extends JpaRepository<JetonUsage, Long> {
}
