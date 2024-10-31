package com.classroom.equipment.dao;

import com.classroom.equipment.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountDao extends JpaRepository<Account, Long> {
}
