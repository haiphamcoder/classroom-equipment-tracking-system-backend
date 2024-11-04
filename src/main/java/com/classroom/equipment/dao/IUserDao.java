package com.classroom.equipment.dao;

import com.classroom.equipment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User, Integer> {
    User findById(int id);
}
