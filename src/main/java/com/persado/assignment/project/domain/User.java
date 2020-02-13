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
public class User {

    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;

    @OneToMany
    @Fetch(FetchMode.JOIN)
    Set<BookRegistration> registrations;

    public User() {
    }
}
