package com.wato.watobackend.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PostRequest {

    private Long postId;

    private Long country;

    private Long category;

    private String title;

    private String content;
}
