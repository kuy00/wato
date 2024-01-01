package com.wato.watobackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wato.watobackend.model.constant.Date;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Post post;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Builder
    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }
}
