package com.wato.watobackend.model;

import com.wato.watobackend.model.constant.Date;
import com.wato.watobackend.model.id.BlockUserId;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Block {

    @EmbeddedId
    private BlockUserId blockUserId;

    @MapsId("blockerId")
    @ManyToOne
    private User blocker;

    @MapsId("blockedId")
    @ManyToOne
    private User blocked;

    private LocalDateTime createDate;

    @Builder
    public Block(User blocker, User blocked, LocalDateTime createDate) {
        this.blockUserId = BlockUserId.builder().blockerId(blocker.getId()).blockedId(blocked.getId()).build();
        this.blocker = blocker;
        this.blocked = blocked;
        this.createDate = createDate;
    }
}
