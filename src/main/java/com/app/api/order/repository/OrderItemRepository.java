package com.app.api.order.repository;

import com.app.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // 추가적인 커스텀 쿼리 메서드를 정의할 수 있습니다.
}
