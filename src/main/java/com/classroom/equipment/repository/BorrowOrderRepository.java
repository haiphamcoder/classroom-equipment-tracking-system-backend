package com.classroom.equipment.repository;

import com.classroom.equipment.common.enums.OrderStatus;
import com.classroom.equipment.entity.BorrowOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowOrderRepository extends JpaRepository<BorrowOrder, Long> {
    @Query("SELECT o FROM borrow_order o WHERE " +
           "(:borrowerName IS NULL OR LOWER(o.borrower.name) LIKE LOWER(CONCAT('%', :borrowerName, '%'))) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:startDate IS NULL OR o.borrowTime >= :startDate) AND " +
           "(:endDate IS NULL OR o.borrowTime <= :endDate)")
    List<BorrowOrder> searchOrders(
        @Param("borrowerName") String borrowerName,
        @Param("status") OrderStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Sort sort
    );
}
