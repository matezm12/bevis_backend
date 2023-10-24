package com.bevis.credits.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "credits_package")
@EqualsAndHashCode(of = {"id"})
public class CreditsPackage {

    @Id
    private Long id;

    @Column(name = "amount_in_units")
    private BigDecimal amountInUnits;

    @Column(name = "product_id")
    private String productId;

}
