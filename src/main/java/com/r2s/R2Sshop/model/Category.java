package com.r2s.R2Sshop.model;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted;

    @PrePersist
    protected void onCreate() {
        if (this.deleted == null) {
            this.deleted = false;
        }
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"category", "variantProducts"})
    private List<Product> products;
}
