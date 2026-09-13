package com.ne.srm.product.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String photo;
    private String namePl;
    private String nameEn;
    private String nameZh;


    private String descriptionPl;
    private String descriptionEn;
    private String descriptionZh;

    private String color;
    private Boolean needCE;
    private Boolean manuals;
    private Boolean stickers;

}
