package com.classroom.equipment.service.impl;

import com.classroom.equipment.entity.Building;
import com.classroom.equipment.repository.BuildingRepository;
import com.classroom.equipment.service.BuildingService;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingServiceImpl(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Override
    public void addBuilding(String buildingName) {
        Building building = Building.builder()
                .buildingName(buildingName)
                .status(true)
                .build();
        buildingRepository.save(building);
    }

    @Override
    public Building getBuildingById(Long id) {
        return null;
    }

    @Override
    public Building getBuildingByName(String buildingName) {
        return buildingRepository.findByBuildingName(buildingName).orElse(null);
    }

    @Override
    public void updateBuilding(Building building) {
        buildingRepository.save(building);
    }

    @Override
    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    @Override
    public void updateBuildingStatus(Long buildingId, boolean status) {
        Building building = buildingRepository.findById(buildingId).orElse(null);
        if (building != null) {
            building.setStatus(status);
            buildingRepository.save(building);
        }
    }
}
