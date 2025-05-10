package com.api.repository;


import com.azenithsolutions.backendapirest.v1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can add custom query methods here if needed
    // For example:
    // Optional<Category> findByName(String name);
    // List<Category> findByActive(boolean active);
}
