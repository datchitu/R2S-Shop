package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String country;

    @OneToMany(mappedBy = "address")
    @JsonManagedReference
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
