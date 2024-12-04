package com.classroom.equipment.service;

import com.classroom.equipment.dtos.request.UpdateBuildingRequest;
import com.classroom.equipment.entity.Building;

public interface BuildingService {
    String addBuilding(String buildingName);

    Building getBuildingById(Long id);

    Building getBuildingByName(String buildingName);

    String updateBuilding(UpdateBuildingRequest request);

    String deleteBuilding(Long id);

}
