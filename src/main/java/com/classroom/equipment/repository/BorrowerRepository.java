package com.classroom.equipment.repository;

import com.classroom.equipment.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    Optional<Borrower> findByName(String name);

    Optional<Borrower> findByEmail(String email);
}
