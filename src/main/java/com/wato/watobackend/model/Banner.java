package com.wato.watobackend.model;

import com.wato.watobackend.model.constant.BannerType;
import com.wato.watobackend.model.constant.Date;
import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Banner extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BannerType type;

    @Column(length = 50)
    private String title;

    private String imageUrl;

    private String linkUrl;
}
