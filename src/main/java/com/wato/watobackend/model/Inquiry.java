package com.wato.watobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Inquiry extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Builder
    public Inquiry(String email, String title, String content, User user) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
