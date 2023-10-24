package com.bevis.asset.product;

import com.bevis.asset.domain.ProductUpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductUpcRepository extends JpaRepository<ProductUpc, String> {
    Page<ProductUpc> findAllByUpcContainsOrNameContainsOrDescriptionContains(String upc, String name, String description, Pageable pageable);
}
