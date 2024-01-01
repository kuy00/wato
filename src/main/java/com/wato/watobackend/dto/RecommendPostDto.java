package com.wato.watobackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendPostDto {

    private List<PostDto> category;

    private List<PostDto> country;
}
