package com.bevis.asset.domain;

import com.bevis.master.domain.Master;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Table(name = "assets_relations")
@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class AssetsRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Master asset;

    @ManyToOne
    @JoinColumn(name = "parent_asset_id")
    private Master parentAsset;
}
