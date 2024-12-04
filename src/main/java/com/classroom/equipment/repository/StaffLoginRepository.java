package com.classroom.equipment.repository;

import com.classroom.equipment.entity.StaffLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffLoginRepository extends JpaRepository<StaffLogin, Long> {
    Optional<StaffLogin> findByUsername(String username);

    Optional<StaffLogin> findByStaffId(Long id);
}
