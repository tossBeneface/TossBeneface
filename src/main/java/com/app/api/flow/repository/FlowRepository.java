package com.app.api.flow.repository;

import com.app.domain.districtAnaly.entity.Flow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlowRepository extends JpaRepository<Flow, Long> {
    Optional<Flow> findTopByOrderByCreatedAtDesc();
}
