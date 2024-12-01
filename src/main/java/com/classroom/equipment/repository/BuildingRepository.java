package com.classroom.equipment.repository;

import com.classroom.equipment.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByBuildingName(String name);
}
