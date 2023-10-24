package com.bevis.master.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Table(name = "assets")
@Data
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Asset {
    @Id
    @Column(name = "asset_id")
    private String id;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "attributes")
    private Map<String, Object> attributes;

    @JsonIgnore
    @OneToOne(mappedBy = "asset", fetch = FetchType.LAZY)
    private Master master;
}
