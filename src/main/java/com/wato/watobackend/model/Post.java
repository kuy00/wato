package com.wato.watobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wato.watobackend.model.constant.Date;
import javax.persistence.*;

import com.wato.watobackend.model.constant.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Country country;

    @OneToOne
    private Category category;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    private User user;

    @JsonIgnore
    private String imageUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    private PostStatus status;

    @Transient
    private String postImageUrl;

    @Builder
    public Post(Country country, Category category, String title, String content, User user, String imageUrl) {
        this.country = country;
        this.category = category;
        this.title = title;
        this.content = content;
        this.user = user;
        this.imageUrl = imageUrl;
        this.status = PostStatus.ENABLED;
    }
}
