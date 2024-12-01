package com.classroom.equipment.repository;

import com.classroom.equipment.entity.BorrowOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowOrderRepository extends JpaRepository<BorrowOrder, Long> {
}
