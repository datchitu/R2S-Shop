package com.r2s.R2Sshop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_line_items")
public class CartLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted = false;

    private Integer quantity = 0;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    @ManyToOne
    @JoinColumn(name = "variant_product_id", nullable = false)
    @JsonIgnoreProperties({"cartLineItems", "product"})
    private VariantProduct variantProduct;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnoreProperties({"cartLineItems", "user", "userVoucher"})
    private Cart cart;
}
