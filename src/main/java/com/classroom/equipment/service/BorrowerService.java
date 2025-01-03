package com.classroom.equipment.service;

import com.classroom.equipment.common.enums.BorrowerStatus;
import com.classroom.equipment.dtos.request.AddBorrowerRequest;
import com.classroom.equipment.entity.Borrower;

import java.util.List;
import java.util.Optional;

public interface BorrowerService {
    Optional<Borrower> findByName(String name);

    List<Borrower> findAll();

    String addBorrower(AddBorrowerRequest request);

    String updateBorrowerStatus(Long id, BorrowerStatus status);
}
