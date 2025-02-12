package com.app.api.order.dto;

import com.app.domain.order.entity.OrderItem;
import java.util.List;

public class OrderRequest {
    private Long memberId;           // 클라이언트에서 전달할 memberId
    private List<OrderItem> items;   // 메뉴 목록
    private int totalAmount;         // 총 금액

    // Getters and Setters
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public int getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}