package com.bevis.credits.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "credits_payment")
@EqualsAndHashCode(of = {"id"})
public class CreditsPayment {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time")
    private Instant dateTime;

    @Column(name = "credits", precision=10, scale=2)
    private BigDecimal credits;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private CreditsBalance creditsBalance;
}
