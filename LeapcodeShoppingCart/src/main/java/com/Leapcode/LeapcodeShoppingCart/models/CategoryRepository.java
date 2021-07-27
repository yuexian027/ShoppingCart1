package com.Leapcode.LeapcodeShoppingCart.models;

import com.Leapcode.LeapcodeShoppingCart.models.data.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    List<Category> findAllByOrderBySortingAsc();

    Category findByCasing(String casing);
}
