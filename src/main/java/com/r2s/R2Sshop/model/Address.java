package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.hypersistence.utils.hibernate.id.Tsid;
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
@Table(name = "addresses", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"street", "city", "user_id", "deleted"})})
public class Address {
    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String phoneNumber;

    private Boolean defaulted = false;

    private Boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "address")
    @JsonIgnoreProperties({"addresses", "user"})
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"addresses", "orders", "password", "roles", "carts", "userVouchers"})
    private User user;

    @Version
    private Long version;
}
