package com.wato.watobackend.model;

import com.wato.watobackend.model.id.ReportPostId;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Report {

    @EmbeddedId
    private ReportPostId reportPostId;

    @MapsId("reporterId")
    @ManyToOne
    private User Reporter;

    @MapsId("postId")
    @ManyToOne
    private Post post;

    @Column(columnDefinition = "LONGTEXT")
    private String reason;

    private LocalDateTime createDate;

    @Builder
    public Report(User reporter, Post post, String reason, LocalDateTime createDate) {
        this.reportPostId = ReportPostId.builder().reporterId(reporter.getId()).postId(post.getId()).build();
        this.Reporter = reporter;
        this.post = post;
        this.reason = reason;
        this.createDate = createDate;
    }
}
