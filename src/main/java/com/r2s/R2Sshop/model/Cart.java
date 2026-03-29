package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", columnDefinition = "double default 0")
    private Double totalPrice;

    private String note;

    @Column(name = "payment_type", columnDefinition = "boolean default false")
    private Boolean paymentType;

    @Column(name = "payment_status", columnDefinition = "boolean default false")
    private Boolean paymentStatus;

    @Column(columnDefinition = "boolean default false")
    private Boolean status;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "paid_at")
    private Timestamp paidAt;

    @OneToMany(mappedBy = "cart")
    @JsonManagedReference
    private List<CartLineItem> cartLineItems;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}
