package com.classroom.equipment.service;

import com.classroom.equipment.common.enums.OrderSortBy;
import com.classroom.equipment.dtos.request.CreateBorrowOrderRequest;
import com.classroom.equipment.dtos.request.ExtendDeadlineRequest;
import com.classroom.equipment.dtos.request.CreateReturnRequest;
import com.classroom.equipment.dtos.request.OrderSearchRequest;
import com.classroom.equipment.dtos.response.BorrowOrderResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

public interface BorrowOrderService {

    String createOrder(CreateBorrowOrderRequest request);

    BorrowOrderResponse getOrderById(Long id);

    String extendDeadline(ExtendDeadlineRequest request);

    List<BorrowOrderResponse> getOrders(String sort, OrderSortBy sortBy);

    List<BorrowOrderResponse> searchOrders(OrderSearchRequest searchRequest);

    String processReturn(CreateReturnRequest request);

    String cancelOrders(List<Long> orderIds);

    ResponseEntity<Resource> exportOrders(String format, OrderSearchRequest searchRequest);
}