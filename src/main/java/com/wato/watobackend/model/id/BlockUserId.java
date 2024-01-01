package com.wato.watobackend.model.id;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class BlockUserId implements Serializable {

    private Long blockerId;

    private Long blockedId;

    @Builder
    public BlockUserId(Long blockerId, Long blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }
}
