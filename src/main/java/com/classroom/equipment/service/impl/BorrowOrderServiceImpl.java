package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.*;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.request.CreateBorrowOrderRequest;
import com.classroom.equipment.dtos.request.ExtendDeadlineRequest;
import com.classroom.equipment.dtos.request.CreateReturnRequest;

import com.classroom.equipment.entity.BorrowOrder;
import com.classroom.equipment.entity.Borrower;
import com.classroom.equipment.entity.Equipment;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.entity.ReturnRecord;
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

import java.time.LocalDateTime;
import java.util.List;

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

        if (borrower.getStatus() != Status.ACTIVE) {
            throw new ApiException("Borrower is " + borrower.getStatus().toString().toLowerCase() + 
                ". Reason: " + borrower.getNote());
        }

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
            .orElseThrow(() -> new ApiException("Equipment not found"));

        Staff staff = staffRepository.findById(request.getStaffId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        BorrowOrder order = BorrowOrder.builder()
            .borrower(borrower)
            .equipment(equipment)
            .staff(staff)
            .borrowTime(request.getBorrowTime())
            .returnDeadline(request.getReturnDeadline())
            .status(OrderStatus.BORROWED)
            .build();

        borrowOrderRepository.save(order);

        return "Borrow order created successfully";
    }

    @Override
    public BorrowOrder getOrderById(Long id) {
        return borrowOrderRepository.findById(id).orElseThrow(
            () -> new ApiException("Order not found")
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
    public List<BorrowOrder> getOrders(String sort, OrderSortBy sortBy) {
        Sort.Direction direction = sort.equalsIgnoreCase("DESC") ?
            Sort.Direction.DESC : Sort.Direction.ASC;

        if (sortBy == null) {
            return borrowOrderRepository.findAll(Sort.by(direction, "id"));
        }

        String sortField = switch (sortBy) {
            case BORROWER -> "borrower.name";
            case EQUIPMENT -> "equipment.name";
            case STATUS -> "status";
            case BORROW_TIME -> "borrowTime";
            case RETURN_DEADLINE -> "returnDeadline";
        };

        return borrowOrderRepository.findAll(Sort.by(direction, sortField));
    }

    @Override
    public List<BorrowOrder> searchOrders(String borrowerName) {
        if (borrowerName == null || borrowerName.isEmpty()) {
            return borrowOrderRepository.findAll();
        }
        return borrowOrderRepository.findByBorrowerNameContainingIgnoreCase(borrowerName);
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
            borrower.setStatus(Status.SUSPENDED);
            borrower.setNote("Suspended due to damaged equipment return");
        } else if (request.getEquipmentStatus() == EquipmentStatus.LOST) {
            borrower.setStatus(Status.LOCKED);
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
} 