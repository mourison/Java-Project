package com.persado.assignment.project.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class BookRegistration {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime loanedAt;

    private LocalDateTime returnedAt;

    private Long userId;

    private Long bookId;

    public BookRegistration(){

    }

}
