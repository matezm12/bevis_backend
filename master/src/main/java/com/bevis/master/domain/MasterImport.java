package com.bevis.master.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MasterImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String keyType;

    private Integer qty;

    @CreatedDate
    private Instant date;

    private Boolean genStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "masterImport", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Master> masters = new HashSet<>();
}
