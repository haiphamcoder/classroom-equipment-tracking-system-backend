package com.classroom.equipment.dao;

import com.classroom.equipment.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReturnRecordDao extends JpaRepository<ReturnRecord, Long> {
}
