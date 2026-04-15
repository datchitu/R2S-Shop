package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_vouchers")
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "status", columnDefinition = "integer default 0")
    private Integer status;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted;

    @PrePersist
    protected void onCreate() {
        if (this.deleted == null) {
            this.deleted = false;
        }
    }

    @Column(name = "used_at")
    private Timestamp usedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"userVouchers", "password",  "roles", "addresses", "carts", "orders"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    @JsonIgnoreProperties("userVouchers")
    private Voucher voucher;

    @OneToMany(mappedBy = "userVoucher")
    @JsonIgnoreProperties({"userVoucher", "user", "cartLineItems"})
    private List<Cart> carts;
}
