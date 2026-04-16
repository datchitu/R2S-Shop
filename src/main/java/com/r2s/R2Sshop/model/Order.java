package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_time", nullable = false)
    private Date deliveryTime;

    private Boolean status = false;

    private Boolean deleted = false;

    @Column(name = "order_datetime", nullable = false)
    private Date orderDatetime;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"orders", "password",  "roles", "addresses", "carts", "userVouchers"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonIgnoreProperties({"orders", "user"})
    private Address address;
}
