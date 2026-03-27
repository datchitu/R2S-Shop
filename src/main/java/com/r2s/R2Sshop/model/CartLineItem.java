package com.r2s.R2Sshop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "cart_line_items")
public class CartLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted;

    @Column(columnDefinition = "integer default 0")
    private Integer quantity;

    @Column(name = "total_price", columnDefinition = "double default 0")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "variant_product_id", nullable = false)
    @JsonBackReference
    private VariantProduct variantProduct;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonManagedReference
    private Cart cart;

}
