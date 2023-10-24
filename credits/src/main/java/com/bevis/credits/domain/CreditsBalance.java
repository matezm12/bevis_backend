package com.bevis.credits.domain;

import com.bevis.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "credits_balance")
public class CreditsBalance {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time")
    private Instant dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "balance", precision=10, scale=2)
    private BigDecimal balance;

    @Column(name = "total_income", precision=10, scale=2)
    private BigDecimal totalIncome;

    @Column(name = "total_outcome", precision=10, scale=2)
    private BigDecimal totalOutcome;

    @JsonIgnore
    @OneToMany(mappedBy = "creditsBalance", fetch = FetchType.LAZY)
    private Set<CreditsCharge> charges = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "creditsBalance", fetch = FetchType.LAZY)
    private Set<CreditsPayment> payments = new HashSet<>();

}
