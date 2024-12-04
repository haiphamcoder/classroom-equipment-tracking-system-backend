package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.Status;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.request.AddBorrowerRequest;
import com.classroom.equipment.entity.Borrower;
import com.classroom.equipment.repository.BorrowerRepository;
import com.classroom.equipment.repository.UserRepository;
import com.classroom.equipment.service.BorrowerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {
    private final BorrowerRepository borrowerRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Borrower> findByName(String name) {
        return borrowerRepository.findByName(name);
    }

    @Override
    @Transactional
    public String addBorrower(AddBorrowerRequest request) {
        Optional<Borrower> borrowerOpt = borrowerRepository.findByEmail(request.getEmail());
        if (borrowerOpt.isPresent()) {
            throw new ApiException("Borrower with this email already exists");
        }

        //TODO add user validation later
//        userRepository.findByEmail(request.getEmail()).orElseThrow(
//            () -> new ApiException("Not a student")
//        );

        Borrower newBorrower = Borrower.builder()
            .name(request.getName())
            .email(request.getEmail())
            .telegramId(request.getTelegramId())
            .type(request.getType())
            .status(Status.ACTIVE)
            .build();
        borrowerRepository.save(newBorrower);

        return "Borrower added successfully";
    }

    @Override
    @Transactional
    public String updateBorrowerStatus(Long id, Status status) {
        Borrower borrower = borrowerRepository.findById(id).orElseThrow(
            () -> new ApiException("Borrower not found")
        );
        borrower.setStatus(status);
        if (status == Status.DELETED) {
            borrower.setDeleted(true);
        }
        borrowerRepository.save(borrower);

        return "Update Borrower status successfully";
    }
}
