package com.Leapcode.LeapcodeShoppingCart.models.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String casing;

    private int sorting;
}
