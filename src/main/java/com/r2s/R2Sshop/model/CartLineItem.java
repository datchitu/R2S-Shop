package com.r2s.R2Sshop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_line_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"variant_product_id", "cart_id", "deleted"})
})
public class CartLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted = false;

    private Integer quantity = 0;

    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "variant_product_id", nullable = false)
    @JsonIgnoreProperties({"cartLineItems", "product"})
    private VariantProduct variantProduct;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnoreProperties({"cartLineItems", "user", "userVoucher"})
    private Cart cart;

    public Boolean isNew() {
        return this.id == null;
    }
}
