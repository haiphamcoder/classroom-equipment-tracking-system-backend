package com.classroom.equipment.dao;

import com.classroom.equipment.entity.StaffLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaffLoginDao extends JpaRepository<StaffLogin, Long> {
}
