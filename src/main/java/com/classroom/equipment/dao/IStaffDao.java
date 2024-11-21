package com.classroom.equipment.dao;

import com.classroom.equipment.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaffDao extends JpaRepository<Staff, Long> {
}
