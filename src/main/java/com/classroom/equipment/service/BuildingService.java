package com.classroom.equipment.service;

import com.classroom.equipment.entity.Building;

public interface BuildingService {
    void addBuilding(String buildingName);

    Building getBuildingById(Long id);

    Building getBuildingByName(String buildingName);

    void updateBuilding(Building building);

    void deleteBuilding(Long id);

    void updateBuildingStatus(Long buildingId, boolean status);
}
