package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Tsid
    private Long id;

    @Column(name = "cart_id", nullable = false, unique = true)
    private Long cartId;

    @Column(name = "delivery_time", nullable = false)
    private LocalDateTime deliveryTime;

    private String note;

    @Column(name = "delivery_status")
    private Boolean deliveryStatus = false;

    private Boolean status = false;

    private Boolean deleted = false;

    @CreationTimestamp
    @Column(name = "order_datetime", nullable = false)
    private LocalDateTime orderDatetime;

    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"orders", "password",  "roles", "addresses", "carts", "userVouchers"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonIgnoreProperties({"orders", "user"})
    private Address address;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties({"order"})
    private List<OrderItem> orderItems;

    @Version
    private Long version;
}
