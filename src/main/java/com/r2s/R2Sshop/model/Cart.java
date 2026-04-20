package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    private String note;

    @Column(name = "payment_type")
    private Boolean paymentType;

    @Column(name = "payment_status")
    private Boolean paymentStatus = false;

    private Boolean status = false;

    private Boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @OneToMany(mappedBy = "cart")
    @JsonIgnoreProperties({"variantProduct", "cart"})
    private List<CartLineItem> cartLineItems;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"carts", "password", "userVouchers", "addresses", "orders", "roles"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_voucher_id")
    @JsonIgnoreProperties({"carts", "user"})
    private UserVoucher userVoucher;
}
