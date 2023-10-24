package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.asset.domain.ProductUpc;
import com.bevis.asset.product.ProductUpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductUpcController {

    private final ProductUpcService productUpcService;

    @Secured(ADMIN)
    @GetMapping("admin/product-upc/{upc}")
    ProductUpc findOne(@PathVariable String upc){
        log.debug("REST to load product-upc {}...", upc);
        return productUpcService.findById(upc)
                .orElseThrow(() -> new ObjectNotFoundException("product-upc with id " + upc + " not found "));
    }

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/product-upc")
    DataResponse<ProductUpc> findAll(String search, Pageable pageable) {
        log.debug("REST to load product-upc...");
        Page<ProductUpc> page = productUpcService.findAll(search, pageable);
        return DataResponse.of(page);
    }

    @Secured(ADMIN)
    @PostMapping("admin/product-upc")
    ProductUpc create(@RequestBody @Valid ProductUpc productUpc){
        log.debug("REST to create product-upc: {}", productUpc);
        return saveProduct(productUpc);
    }

    @Secured(ADMIN)
    @PutMapping("admin/product-upc")
    ProductUpc update(@RequestBody @Valid ProductUpc productUpc){
        log.debug("REST to update product-upc: {}", productUpc);
        if (Objects.isNull(productUpc.getUpc())){
            throw new RuntimeException("Id is null");
        }
        return saveProduct(productUpc);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/product-upc/{upc}")
    void delete(@PathVariable String upc){
        log.debug("REST to delete product-upc by ID: {}", upc);
        productUpcService.deleteById(upc);
    }

    private ProductUpc saveProduct(ProductUpc productUpc) {
        if (Strings.isBlank(productUpc.getAssetId())) {
            productUpc.setAssetId(null);
        }
        return productUpcService.save(productUpc);
    }

}
