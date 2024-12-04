package com.classroom.equipment.service;

import com.classroom.equipment.common.enums.OrderSortBy;
import com.classroom.equipment.dtos.request.CreateBorrowOrderRequest;
import com.classroom.equipment.dtos.request.ExtendDeadlineRequest;
import com.classroom.equipment.dtos.request.CreateReturnRequest;
import com.classroom.equipment.dtos.response.BorrowOrderResponse;

import java.util.List;

public interface BorrowOrderService {
    String createOrder(CreateBorrowOrderRequest request);
    BorrowOrderResponse getOrderById(Long id);
    String extendDeadline(ExtendDeadlineRequest request);
    String cancelOrder(Long orderId);
    List<BorrowOrderResponse> getOrders(String sort, OrderSortBy sortBy);
    List<BorrowOrderResponse> searchOrders(String borrowerName);
    String processReturn(CreateReturnRequest request);
}