package com.app.api.flow.repository;

import com.app.domain.analy.entity.DistrictFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlowRepository extends JpaRepository<DistrictFlow, Long> {
    Optional<DistrictFlow> findTopByOrderByCreatedAtDesc();
}
