package com.bevis.credits.domain;

import com.bevis.credits.domain.enumeration.CreditsChargeSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "credits_charge")
@EqualsAndHashCode(of = {"id"})
public class CreditsCharge {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time")
    private Instant dateTime;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "purchase_id")
    private String purchaseId;

    @Column(name = "credits", precision=10, scale=2)
    private BigDecimal credits;

    @Column(name = "source")
    @Enumerated(EnumType.STRING)
    private CreditsChargeSource source;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private CreditsBalance creditsBalance;
}
