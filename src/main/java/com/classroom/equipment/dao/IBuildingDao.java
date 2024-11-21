package com.classroom.equipment.dao;

import com.classroom.equipment.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBuildingDao extends JpaRepository<Building, Long> {

}
