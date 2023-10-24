package com.bevis.asset.product;

import com.bevis.asset.domain.ProductUpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductUpcService {

    Optional<ProductUpc> findById(String upc);

    Page<ProductUpc> findAll(String search, Pageable pageable);

    ProductUpc save(ProductUpc productUpc);

    void deleteById(String upc);
}
