package com.r2s.R2Sshop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "cart_line_item")
public class CartLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 0)
    private Boolean deleted;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "variant_product_id", nullable = false)
    @JsonBackReference
    private VariantProduct variant_product;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonManagedReference
    private Cart cart;

}
