package com.classroom.equipment.controller;

import com.classroom.equipment.common.enums.OrderSortBy;
import com.classroom.equipment.dtos.request.CreateBorrowOrderRequest;
import com.classroom.equipment.dtos.request.ExtendDeadlineRequest;
import com.classroom.equipment.dtos.request.CreateReturnRequest;
import com.classroom.equipment.entity.BorrowOrder;
import com.classroom.equipment.service.BorrowOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class BorrowOrderController {
    private final BorrowOrderService borrowOrderService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody CreateBorrowOrderRequest request) {
        return ResponseEntity.ok(borrowOrderService.createOrder(request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BorrowOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowOrderService.getOrderById(id));
    }

    @PostMapping("/extend-deadline")
    public ResponseEntity<String> extendDeadline(@RequestBody ExtendDeadlineRequest request) {
        return ResponseEntity.ok(borrowOrderService.extendDeadline(request));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(borrowOrderService.cancelOrder(orderId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BorrowOrder>> getOrders(
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) OrderSortBy sortBy) {
        return ResponseEntity.ok(borrowOrderService.getOrders(sort, sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BorrowOrder>> searchOrders(
            @RequestParam(required = false) String borrowerName) {
        return ResponseEntity.ok(borrowOrderService.searchOrders(borrowerName));
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnOrder(@RequestBody CreateReturnRequest request) {
        return ResponseEntity.ok(borrowOrderService.processReturn(request));
    }
}
