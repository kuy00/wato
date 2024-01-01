package com.wato.watobackend.model;

import com.wato.watobackend.model.constant.Date;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Announcement extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Builder
    public Announcement(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
