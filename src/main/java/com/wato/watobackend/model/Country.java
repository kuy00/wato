package com.wato.watobackend.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;
}
