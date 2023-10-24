package com.bevis.lister;


import com.bevis.master.domain.Master;
import com.bevis.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "lister_assets")
public class ListerAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Master master;
}
