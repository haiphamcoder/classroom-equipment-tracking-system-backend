package com.classroom.equipment.repository;

import com.classroom.equipment.entity.BorrowOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowOrderRepository extends JpaRepository<BorrowOrder, Long> {
    List<BorrowOrder> findByBorrowerNameContainingIgnoreCase(String borrowerName);
}
