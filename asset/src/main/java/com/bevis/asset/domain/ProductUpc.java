package com.bevis.asset.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@Entity
@Table(name = "product_upc")
@EntityListeners(AuditingEntityListener.class)
public class ProductUpc {

    @Id
    @Size(max = 12)
    @Column(name = "upc")
    private String upc;

    @Column(name = "asset_id")
    private String assetId;

    @Size(max = 255)
    @Column(name = "`name`")
    private String name;

    @Column(name = "`description`")
    private String description;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
