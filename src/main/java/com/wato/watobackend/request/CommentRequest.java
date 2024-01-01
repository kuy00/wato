package com.wato.watobackend.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommentRequest {

    private Long commentId;

    @NotEmpty
    private String content;
}
