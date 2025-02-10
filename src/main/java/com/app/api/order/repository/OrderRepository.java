package com.app.api.order.repository;

import com.app.domain.order.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderPayment, Long> {
}
