package com.bevis.config.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Config {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "`key`")
    private String key;

    @Column(name = "`value`")
    private String value;

    @Column(name = "description")
    private String description;
}
