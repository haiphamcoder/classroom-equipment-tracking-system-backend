package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.*;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.request.CreateBorrowOrderRequest;
import com.classroom.equipment.dtos.request.ExtendDeadlineRequest;
import com.classroom.equipment.dtos.request.CreateReturnRequest;
import com.classroom.equipment.dtos.response.BorrowOrderResponse;
import com.classroom.equipment.dtos.response.OrderItemResponse;

import com.classroom.equipment.entity.BorrowOrder;
import com.classroom.equipment.entity.Borrower;
import com.classroom.equipment.entity.Equipment;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.entity.ReturnRecord;
import com.classroom.equipment.entity.OrderItem;
import com.classroom.equipment.repository.BorrowOrderRepository;
import com.classroom.equipment.repository.BorrowerRepository;
import com.classroom.equipment.repository.EquipmentRepository;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.repository.ReturnRecordRepository;
import com.classroom.equipment.service.BorrowOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowOrderServiceImpl implements BorrowOrderService {
    private final BorrowOrderRepository borrowOrderRepository;
    private final BorrowerRepository borrowerRepository;
    private final EquipmentRepository equipmentRepository;
    private final StaffRepository staffRepository;
    private final ReturnRecordRepository returnRecordRepository;

    @Override
    @Transactional
    public String createOrder(CreateBorrowOrderRequest request) {
        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
            .orElseThrow(() -> new ApiException("Borrower not found"));

        Staff staff = staffRepository.findById(request.getStaffId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        BorrowOrder order = BorrowOrder.builder()
            .borrower(borrower)
            .staff(staff)
            .borrowTime(request.getBorrowTime())
            .returnDeadline(request.getReturnDeadline())
            .status(OrderStatus.BORROWED)
            .build();

        List<OrderItem> orderItems = request.getItems()
            .stream()
            .map(itemRequest -> {
                Equipment equipment = equipmentRepository.findById(itemRequest.getEquipmentId())
                .orElseThrow(() -> new ApiException("Equipment not found"));

            return OrderItem.builder()
                .order(order)
                .equipment(equipment)
                .quantity(itemRequest.getQuantity())
                .status(EquipmentStatus.BORROWED)
                .notes(itemRequest.getNotes())
                .build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        borrowOrderRepository.save(order);

        return "Borrow order created successfully";
    }

    @Override
    public BorrowOrderResponse getOrderById(Long id) {
        BorrowOrder order = borrowOrderRepository.findById(id)
            .orElseThrow(() -> new ApiException("Order not found"));

        List<OrderItemResponse> itemResponses = toOrderItemResponse(order.getOrderItems());

        return new BorrowOrderResponse(
            order.getId(),
            order.getBorrower().getName(),
            order.getStaff().getName(),
            order.getBorrowTime(),
            order.getReturnDeadline(),
            order.getStatus(),
            itemResponses
        );
    }

    @Override
    @Transactional
    public String extendDeadline(ExtendDeadlineRequest request) {
        BorrowOrder order = borrowOrderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new ApiException("Order not found"));

        if (order.getStatus() != OrderStatus.BORROWED) {
            throw new ApiException("Can only extend deadline for borrowed orders");
        }

        if (request.getNewDeadline().isBefore(LocalDateTime.now())) {
            throw new ApiException("New deadline must be in the future");
        }

        order.setReturnDeadline(request.getNewDeadline());
        borrowOrderRepository.save(order);

        return "Deadline extended successfully";
    }

    @Override
    @Transactional
    public String cancelOrder(Long orderId) {
        BorrowOrder order = borrowOrderRepository.findById(orderId)
            .orElseThrow(() -> new ApiException("Order not found"));

        if (order.getStatus() != OrderStatus.BORROWED) {
            throw new ApiException("Can only cancel un-return orders");
        }

        order.setStatus(OrderStatus.CANCELLED);
        borrowOrderRepository.save(order);

        return "Order cancelled successfully";
    }

    @Override
    public List<BorrowOrderResponse> getOrders(String sort, OrderSortBy sortBy) {
        Sort.Direction direction = sort.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;

        if (sortBy == null) {
            return borrowOrderRepository.findAll(Sort.by(direction, "id"))
                .stream()
                .map(this::toBorrowOrderResponse)
                .collect(Collectors.toList());
        }

        String sortField = switch (sortBy) {
            case BORROWER -> "borrower.name";
            case EQUIPMENT -> "equipment.name";
            case STATUS -> "status";
            case BORROW_TIME -> "borrowTime";
            case RETURN_DEADLINE -> "returnDeadline";
        };

        return borrowOrderRepository.findAll(Sort.by(direction, sortField))
            .stream()
            .map(this::toBorrowOrderResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<BorrowOrderResponse> searchOrders(String borrowerName) {
        if (borrowerName.isEmpty()) {
            return borrowOrderRepository.findAll()
                .stream()
                .map(this::toBorrowOrderResponse)
                .collect(Collectors.toList());
        }
        return borrowOrderRepository.findByBorrowerNameContainingIgnoreCase(borrowerName)
            .stream()
            .map(this::toBorrowOrderResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String processReturn(CreateReturnRequest request) {
        BorrowOrder order = borrowOrderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new ApiException("Order not found"));

        if (order.getStatus() != OrderStatus.BORROWED) {
            throw new ApiException("Can only process return for borrowed orders");
        }

        Staff staff = staffRepository.findById(request.getStaffId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        ReturnStatus returnStatus = switch (request.getEquipmentStatus()) {
            case DAMAGED -> ReturnStatus.REJECTED;
            case NORMAL -> ReturnStatus.COMPLETED;
            case LOST -> ReturnStatus.PENDING;
            default -> throw new ApiException("Invalid equipment status for return");
        };

        Borrower borrower = order.getBorrower();
        if (request.getEquipmentStatus() == EquipmentStatus.DAMAGED) {
            borrower.setStatus(BorrowerStatus.SUSPENDED);
            borrower.setNote("Suspended due to damaged equipment return");
        } else if (request.getEquipmentStatus() == EquipmentStatus.LOST) {
            borrower.setStatus(BorrowerStatus.LOCKED);
            borrower.setNote("Locked due to lost equipment");
        }
        borrowerRepository.save(borrower);

        ReturnRecord returnRecord = ReturnRecord.builder()
            .order(order)
            .staff(staff)
            .returnTime(LocalDateTime.now())
            .equipmentStatus(request.getEquipmentStatus())
            .status(returnStatus)
            .build();

        returnRecordRepository.save(returnRecord);

        if (returnStatus == ReturnStatus.COMPLETED) {
            order.setStatus(OrderStatus.RETURNED);
            borrowOrderRepository.save(order);
        }

        return switch (returnStatus) {
            case COMPLETED -> "Return processed successfully";
            case REJECTED -> "Return rejected due to damaged equipment. Borrower has been suspended.";
            case PENDING -> "Return pending review due to lost equipment. Borrower has been locked.";
        };
    }

    private List<OrderItemResponse> toOrderItemResponse(List<OrderItem> orderItems) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        orderItems.forEach(item -> {
            Equipment equipment = item.getEquipment();
            OrderItemResponse response = OrderItemResponse.builder()
                .id(item.getId())
                .equipmentName(equipment.getName())
                .equipmentRoomName(equipment.getRoom().getRoomName())
                .quantity(item.getQuantity())
                .status(item.getStatus())
                .notes(item.getNotes())
                .build();
            orderItemResponses.add(response);
        });
        return orderItemResponses;
    }

    private BorrowOrderResponse toBorrowOrderResponse(BorrowOrder order) {
        return BorrowOrderResponse.builder()
            .id(order.getId())
            .borrowerName(order.getBorrower().getName())
            .staffName(order.getStaff().getName())
            .borrowTime(order.getBorrowTime())
            .returnDeadline(order.getReturnDeadline())
            .status(order.getStatus())
            .items(toOrderItemResponse(order.getOrderItems()))
            .build();
    }
} 