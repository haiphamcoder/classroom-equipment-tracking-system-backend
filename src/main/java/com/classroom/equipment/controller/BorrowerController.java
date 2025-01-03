package com.classroom.equipment.controller;

import com.classroom.equipment.common.enums.BorrowerStatus;
import com.classroom.equipment.dtos.request.AddBorrowerRequest;
import com.classroom.equipment.entity.Borrower;
import com.classroom.equipment.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/borrower")
@RequiredArgsConstructor
public class BorrowerController {
    private final BorrowerService borrowerService;

    @GetMapping("/get/{name}")
    public ResponseEntity<Optional<Borrower>> getBorrowerByName(@PathVariable String name) {
        return ResponseEntity.ok(borrowerService.findByName(name));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Borrower>> getAllBorrowers() {
        return ResponseEntity.ok(borrowerService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBorrower(@RequestBody(required = false) AddBorrowerRequest request) {
        return ResponseEntity.ok(borrowerService.addBorrower(request));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateBorrowerStatus(
        @RequestParam Long id,
        @RequestParam BorrowerStatus status
    ) {
        return ResponseEntity.ok(borrowerService.updateBorrowerStatus(id, status));
    }

}
