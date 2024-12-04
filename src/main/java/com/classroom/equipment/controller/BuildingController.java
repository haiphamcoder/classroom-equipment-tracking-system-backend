package com.classroom.equipment.controller;

import com.classroom.equipment.dtos.request.UpdateBuildingRequest;
import com.classroom.equipment.entity.Building;
import com.classroom.equipment.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/building")
@RequiredArgsConstructor
public class BuildingController {
    private final BuildingService buildingService;

    @PostMapping("/add")
    public ResponseEntity<String> addBuilding(@RequestParam String buildingName) {
        return ResponseEntity.ok(buildingService.addBuilding(buildingName));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Building> getBuildingByName(@PathVariable String name) {
        return ResponseEntity.ok(buildingService.getBuildingByName(name));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateBuilding(@RequestBody(required = false) UpdateBuildingRequest request) {
        return ResponseEntity.ok(buildingService.updateBuilding(request));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteBuilding(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.deleteBuilding(id));
    }
}
