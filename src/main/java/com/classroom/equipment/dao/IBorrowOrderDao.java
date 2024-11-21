package com.classroom.equipment.dao;

import com.classroom.equipment.entity.BorrowOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBorrowOrderDao extends JpaRepository<BorrowOrder, Long> {
}
