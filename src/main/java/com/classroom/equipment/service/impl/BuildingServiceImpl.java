package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.Status;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.request.UpdateBuildingRequest;
import com.classroom.equipment.entity.Building;
import com.classroom.equipment.repository.BuildingRepository;
import com.classroom.equipment.service.BuildingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;

    @Override
    public String addBuilding(String buildingName) {
        Optional<Building> buildingOpt = buildingRepository.findByBuildingName(buildingName);
        if (buildingOpt.isPresent()) {
            throw new ApiException("Building already exists");
        }
        Building building = Building.builder()
                .buildingName(buildingName)
                .status(Status.AVAILABLE)
                .build();
        buildingRepository.save(building);

        return "Building successfully added";
    }

    @Override
    public Building getBuildingById(Long id) {
        return buildingRepository.findById(id).orElseThrow(
            () -> new ApiException("Building not found")
        );
    }

    @Override
    public Building getBuildingByName(String buildingName) {
        return buildingRepository.findByBuildingName(buildingName).orElseThrow(
            () -> new ApiException("Building not found")
        );
    }

    @Override
    @Transactional
    public String updateBuilding(UpdateBuildingRequest request) {
        Building building = getBuildingById(request.getId());
        if (request.getBuildingName() != null) {
            building.setBuildingName(request.getBuildingName());
        }
        if (request.getAvailable() != null) {
            building.setStatus(request.getAvailable() ? Status.AVAILABLE : Status.UNAVAILABLE);
        }
        buildingRepository.save(building);

        return "Building successfully updated";
    }

    @Override
    public String deleteBuilding(Long id) {
        Building building = getBuildingById(id);
        building.setDeleted(true);
        buildingRepository.save(building);

        return "Building successfully deleted";
    }

}
