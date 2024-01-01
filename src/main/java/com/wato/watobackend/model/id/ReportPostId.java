package com.wato.watobackend.model.id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class ReportPostId implements Serializable {

    private Long reporterId;

    private Long postId;

    @Builder
    public ReportPostId(Long reporterId, Long postId) {
        this.reporterId = reporterId;
        this.postId = postId;
    }
}
