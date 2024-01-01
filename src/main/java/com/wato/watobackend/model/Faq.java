package com.wato.watobackend.model;

import com.wato.watobackend.model.constant.Date;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Faq extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Builder
    public Faq(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
