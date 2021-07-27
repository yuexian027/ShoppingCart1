package com.Leapcode.LeapcodeShoppingCart.models.data;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name="products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private String casing;

    private String description;

    private String image;

    @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "Expected format: 5, 5.99, 10")
    private String price;

    @Pattern(regexp = "^[1-9][0-9]*", message = "Please choose a category")
    @Column(name="category_id")
    private String categoryId;

    @Column(name="created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
