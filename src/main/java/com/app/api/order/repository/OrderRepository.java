package com.app.api.order.repository;

import com.app.domain.member.entity.Member;
import com.app.domain.order.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderPayment, Long> {
    List<OrderPayment> findByMember(Member member);
}
