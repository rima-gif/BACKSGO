package com.rima.ryma_prj.domain.repository;

import com.rima.ryma_prj.domain.model.Robot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotRepository extends JpaRepository<Robot ,Long> {
}
