package com.wato.watobackend.controller;

import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.dto.PostDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Comment;
import com.wato.watobackend.model.Post;
import com.wato.watobackend.request.CommentRequest;
import com.wato.watobackend.request.PostRequest;
import com.wato.watobackend.request.ReportRequest;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.CommentService;
import com.wato.watobackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ApiResponse getPost(
            @PathVariable Long postId)
    {
        PostDto post = postService.getPost(postId);

        return ApiResponse.builder().data(post).build();
    }

    @PostMapping
    public ApiResponse createPost(
            Authentication authentication,
            @RequestPart(value = "request", required = false) PostRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file)
    {
        if (request == null) throw new ApiException(Error.EMPTY_DATA);

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Post post = postService.createPost(Long.parseLong(userDetails.getUsername()), request, file);

            return ApiResponse.builder().data(post).build();
        } catch (ApiException e) {
          throw new ApiException(e.error);
        } catch (Exception e) {
            throw new ApiException(Error.SERVER_ERROR);
        }
    }

    @PutMapping("/{postId}")
    public ApiResponse updatePost(
            Authentication authentication,
            @PathVariable Long postId,
            @RequestPart(value = "request", required = false) PostRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file)
    {
        if (request == null) throw new ApiException(Error.EMPTY_DATA);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Post post = postService.updatePost(Long.parseLong(userDetails.getUsername()), postId, request, file);

        return ApiResponse.builder().data(post).build();
    }

    @DeleteMapping
    public ApiResponse deletePost(
            Authentication authentication,
            @RequestBody(required = false) PostRequest request)
    {
        if (request == null || request.getPostId() == null) throw new ApiException(Error.EMPTY_DATA);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean deletePost = postService.deletePost(Long.parseLong(userDetails.getUsername()), request.getPostId());

        return ApiResponse.builder().data(deletePost).build();
    }

    @GetMapping("/{postId}/comment")
    public ApiResponse getComment(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (page > 0) {
            page = page - 1;
        } else {
            page = 0;
        }

        PageDto comments = commentService.getComments(postId, page, size);

        return ApiResponse.builder().data(comments).build();
    }

    @PostMapping("/{postId}/comment")
    public ApiResponse createComment(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody(required = false) CommentRequest request
    ){
        if (request == null || !StringUtils.hasText(request.getContent())) throw new ApiException(Error.EMPTY_DATA);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Comment comment = commentService.createComment(Long.parseLong(userDetails.getUsername()), postId, request.getContent());

        return ApiResponse.builder().data(comment).build();
    }

    @PutMapping("/{postId}/comment/{commentId}")
    public ApiResponse updateComment(
            Authentication authentication,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody(required = false) CommentRequest request
    ){
        if (request == null) throw new ApiException(Error.EMPTY_DATA);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Comment comment = commentService.updateComment(Long.parseLong(userDetails.getUsername()), postId, commentId, request);

        return ApiResponse.builder().data(comment).build();
    }

    @DeleteMapping("/{postId}/comment")
    public ApiResponse deleteComment(
            Authentication authentication,
            @PathVariable Long postId,
            @RequestBody(required = false) CommentRequest request
    ){
        if (request == null || request.getCommentId() == null) throw new ApiException(Error.EMPTY_DATA);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean deleteComment = commentService.deleteComment(Long.parseLong(userDetails.getUsername()), postId, request.getCommentId());

        return ApiResponse.builder().data(deleteComment).build();
    }

    @PostMapping("/{postId}/report")
    public ApiResponse postReport(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody(required = false)  ReportRequest request
    ){
        if (request == null || !StringUtils.hasText(request.getReason())) throw new ApiException(Error.EMPTY_DATA);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean postReport = postService.postReport(Long.parseLong(userDetails.getUsername()), postId, request.getReason());

        return ApiResponse.builder().data(postReport).build();
    }
}
