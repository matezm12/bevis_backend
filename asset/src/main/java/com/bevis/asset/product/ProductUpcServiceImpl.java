package com.bevis.asset.product;

import com.bevis.asset.domain.ProductUpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
class ProductUpcServiceImpl implements ProductUpcService {

    private final ProductUpcRepository productUpcRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<ProductUpc> findById(String upc) {
        return productUpcRepository.findById(upc);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductUpc> findAll(String search, Pageable pageable) {
        String s = Objects.nonNull(search) ? search: "";
        return productUpcRepository.findAllByUpcContainsOrNameContainsOrDescriptionContains(s, s, s, pageable);
    }

    @Override
    public ProductUpc save(ProductUpc productUpc) {
        return productUpcRepository.save(productUpc);
    }

    @Override
    public void deleteById(String upc) {
        productUpcRepository.deleteById(upc);
    }
}
