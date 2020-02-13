package com.persado.assignment.project.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Book {

    @Id
    private Long isbn;
    private String name;
    private String summary;
    private int purchased;
    private int available;

    @OneToMany
    @Fetch(FetchMode.SELECT)
    private Set<BookRegistration> registrations;

    private Book() {
    }

}
