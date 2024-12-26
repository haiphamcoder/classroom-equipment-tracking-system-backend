package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.*;
import com.classroom.equipment.config.exception.ApiException;
import com.classroom.equipment.dtos.export.BorrowOrderExportDTO;
import com.classroom.equipment.dtos.request.*;
import com.classroom.equipment.dtos.response.BorrowOrderResponse;
import com.classroom.equipment.dtos.response.OrderItemResponse;

import com.classroom.equipment.entity.*;
import com.classroom.equipment.repository.BorrowOrderRepository;
import com.classroom.equipment.repository.BorrowerRepository;
import com.classroom.equipment.repository.EquipmentRepository;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.repository.ReturnRecordRepository;
import com.classroom.equipment.service.BorrowOrderService;
import com.classroom.equipment.service.ExportService;
import com.classroom.equipment.service.NotificationSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowOrderServiceImpl implements BorrowOrderService {
    private final BorrowOrderRepository borrowOrderRepository;
    private final BorrowerRepository borrowerRepository;
    private final EquipmentRepository equipmentRepository;
    private final StaffRepository staffRepository;
    private final ReturnRecordRepository returnRecordRepository;
    private final ExportService exportService;
    private final NotificationSchedulerService notificationSchedulerService;

    @Override
    @Transactional
    public String createOrder(CreateBorrowOrderRequest request) {
        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
            .orElseThrow(() -> new ApiException("Borrower not found"));

        Staff staff = staffRepository.findById(request.getStaffId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        if (borrower.getStatus() != BorrowerStatus.ACTIVE) {
            throw new ApiException("Borrower account is not active");
        }

        for (OrderItemRequest itemRequest : request.getItems()) {
            Equipment equipment = equipmentRepository.findById(itemRequest.getEquipmentId())
                .orElseThrow(() -> new ApiException("Equipment not found"));
                
            if (equipment.getQuantity() < itemRequest.getQuantity()) {
                throw new ApiException("Insufficient quantity for equipment: " + equipment.getName());
            }
            
            equipment.setQuantity(equipment.getQuantity() - itemRequest.getQuantity());
            if (equipment.getQuantity() == 0) {
                equipment.setStatus(EquipmentStatus.UNAVAILABLE);
            }
            equipmentRepository.save(equipment);
        }

        BorrowOrder order = BorrowOrder.builder()
            .borrower(borrower)
            .staff(staff)
            .borrowTime(request.getBorrowTime())
            .returnDeadline(request.getReturnDeadline())
            .status(OrderStatus.BORROWED)
            .build();

        List<OrderItem> orderItems = createOrderItems(request.getItems(), order);
        order.setOrderItems(orderItems);
        BorrowOrder savedOrder = borrowOrderRepository.save(order);
        log.info("Borrow order created successfully");
        NotificationSchedule notificationSchedule = notificationSchedulerService.createNotificationScheduleFromBorrowOrder(savedOrder);
        if (notificationSchedule!=null){
            log.info("Notification schedule created successfully");
        }

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
    public List<BorrowOrderResponse> getOrders(String sort, OrderSortBy sortBy) {
        Sort.Direction direction = sort.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;

        String sortField = sortBy == null ? "id" : switch (sortBy) {
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
    public List<BorrowOrderResponse> searchOrders(OrderSearchRequest request) {
        Sort.Direction direction = request.getSortDirection().equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;

        String sortField = request.getSortBy() == null ? "id" : switch (request.getSortBy()) {
            case BORROWER -> "borrower.name";
            case EQUIPMENT -> "orderItems.equipment.name";
            case STATUS -> "status";
            case BORROW_TIME -> "borrowTime";
            case RETURN_DEADLINE -> "returnDeadline";
        };

        List<BorrowOrder> orders = borrowOrderRepository.searchOrders(
            request.getBorrowerName(),
            request.getStatus(),
            request.getStartDate(),
            request.getEndDate(),
            Sort.by(direction, sortField)
        );
        
        return orders.stream()
            .map(this::mapToResponse)
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

        Map<Long, OrderItem> orderItemMap = order.getOrderItems().stream()
            .collect(Collectors.toMap(OrderItem::getId, item -> item));

        boolean hasProblematicReturn = false;
        boolean allItemsReturned = true;

        for (CreateReturnRequest.ReturnItemRequest returnItem : request.getItems()) {
            OrderItem orderItem = orderItemMap.get(returnItem.getOrderItemId());
            if (orderItem == null) {
                throw new ApiException("Order item not found: " + returnItem.getOrderItemId());
            }

            if (returnItem.getReturnQuantity() > orderItem.getQuantity()) {
                throw new ApiException("Return quantity exceeds borrowed quantity for item: " + orderItem.getEquipment().getName());
            }

            Equipment equipment = orderItem.getEquipment();
            
            if (returnItem.getStatus() == EquipmentStatus.NORMAL) {
                equipment.setQuantity(equipment.getQuantity() + returnItem.getReturnQuantity());
                if (equipment.getQuantity() > 0) {
                    equipment.setStatus(EquipmentStatus.AVAILABLE);
                }
            } else {
                hasProblematicReturn = true;
            }
            equipmentRepository.save(equipment);

            orderItem.setReturnTime(LocalDateTime.now());
            orderItem.setStatus(returnItem.getStatus());
            orderItem.setNotes(returnItem.getNotes());
            
            if (returnItem.getReturnQuantity() < orderItem.getQuantity()) {
                allItemsReturned = false;
            }
        }

        Borrower borrower = order.getBorrower();
        if (hasProblematicReturn) {
            boolean hasLostItems = request.getItems().stream()
                .anyMatch(item -> item.getStatus() == EquipmentStatus.LOST);
            
            if (hasLostItems) {
                borrower.setStatus(BorrowerStatus.LOCKED);
                borrower.setNote("Locked due to lost equipment");
            } else {
                borrower.setStatus(BorrowerStatus.SUSPENDED);
                borrower.setNote("Suspended due to damaged equipment return");
            }
            borrowerRepository.save(borrower);
        }

        ReturnStatus returnStatus = hasProblematicReturn ?
            (borrower.getStatus() == BorrowerStatus.LOCKED ? ReturnStatus.PENDING : ReturnStatus.REJECTED) :
            ReturnStatus.COMPLETED;

        ReturnRecord returnRecord = ReturnRecord.builder()
            .order(order)
            .staff(staff)
            .returnTime(LocalDateTime.now())
            .equipmentStatus(hasProblematicReturn ? EquipmentStatus.DAMAGED : EquipmentStatus.NORMAL)
            .status(returnStatus)
            .build();
        returnRecordRepository.save(returnRecord);

        if (allItemsReturned) {
            order.setStatus(OrderStatus.RETURNED);
            borrowOrderRepository.save(order);
        }

        return switch (returnStatus) {
            case COMPLETED -> "Return processed successfully";
            case REJECTED -> "Return rejected due to damaged equipment. Borrower has been suspended.";
            case PENDING -> "Return pending review due to lost equipment. Borrower has been locked.";
        };
    }

    @Override
    @Transactional
    public String cancelOrders(List<Long> orderIds) {
        List<BorrowOrder> orders = borrowOrderRepository.findAllById(orderIds);
        
        if (orders.isEmpty()) {
            throw new ApiException("No orders found with provided ids");
        }

        List<BorrowOrder> invalidOrders = orders.stream()
            .filter(order -> order.getStatus() != OrderStatus.BORROWED)
            .toList();
        
        if (!invalidOrders.isEmpty()) {
            throw new ApiException("Cannot cancel orders that are already returned or cancelled: " + 
                invalidOrders.stream()
                    .map(order -> "Order #" + order.getId())
                    .collect(Collectors.joining(", ")));
        }

        orders.forEach(order -> {
            order.getOrderItems().forEach(item -> {
                Equipment equipment = item.getEquipment();
                if (equipment != null) {
                    equipment.setQuantity(equipment.getQuantity() + item.getQuantity());
                    if (equipment.getQuantity() > 0) {
                        equipment.setStatus(EquipmentStatus.AVAILABLE);
                    }
                    equipmentRepository.save(equipment);
                }
            });
            
            order.setStatus(OrderStatus.CANCELLED);
        });
        
        borrowOrderRepository.saveAll(orders);
        return "Cancelled " + orders.size() + " orders successfully";
    }

    @Override
    public ResponseEntity<Resource> exportOrders(String format, OrderSearchRequest searchRequest) {
        List<BorrowOrderResponse> orders;
        if (searchRequest != null) {
            orders = searchOrders(searchRequest);
        } else {
            orders = getOrders("ASC", OrderSortBy.BORROW_TIME);
        }
        
        List<BorrowOrderExportDTO> exportData = orders.stream()
            .map(BorrowOrderExportDTO::from)
            .collect(Collectors.toList());
        
        String filename = String.format("Borrow_Orders_Report_%s", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")));
            
        return exportService.exportToFile(exportData, filename, format);
    }

    private List<OrderItemResponse> toOrderItemResponse(List<OrderItem> orderItems) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        orderItems.forEach(item -> {
            Equipment equipment = item.getEquipment();
            OrderItemResponse response = OrderItemResponse.builder()
                .id(item.getId())
                .equipmentName(equipment != null ? equipment.getName() : null)
                .equipmentRoomName(equipment != null ? equipment.getRoom().getRoomName() : null)
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

    private BorrowOrderResponse mapToResponse(BorrowOrder order) {
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

    private List<OrderItem> createOrderItems(List<OrderItemRequest> itemRequests, BorrowOrder order) {
        return itemRequests.stream()
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
            })
            .collect(Collectors.toList());
    }
} 