package com.r2s.R2Sshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String phone;

    @Column(columnDefinition = "boolean default false")
    private Boolean status;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted;

    @PrePersist
    protected void onCreate() {
        if (this.deleted == null) {
            this.deleted = false;
        }
        if (this.status == null) {
            this.status = false;
        }
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "userVoucher"})
    private List<Cart> carts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("users")
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "address"})
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "orders"})
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "voucher", "carts"})
    private List<UserVoucher> userVouchers;
}
