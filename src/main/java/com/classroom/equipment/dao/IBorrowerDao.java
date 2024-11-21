package com.classroom.equipment.dao;

import com.classroom.equipment.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBorrowerDao extends JpaRepository<Borrower, Long> {
}
