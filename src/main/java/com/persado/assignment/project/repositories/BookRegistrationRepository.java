package com.persado.assignment.project.repositories;

import com.persado.assignment.project.domain.BookRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRegistrationRepository extends JpaRepository<BookRegistration, Long> {
}
