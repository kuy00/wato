package com.wato.watobackend.service;

import com.wato.watobackend.dto.CommentDto;
import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Comment;
import com.wato.watobackend.model.Post;
import com.wato.watobackend.model.User;
import com.wato.watobackend.model.constant.NotificationType;
import com.wato.watobackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

//    private final NotificationService notificationService;
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public PageDto getComments(Long postId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CommentDto> comments = commentRepository.findAllPage(postId, pageRequest);

        return PageDto.builder()
                .list(comments.getContent())
                .page(comments.getPageable().getPageNumber() + 1)
                .size(comments.getPageable().getPageSize())
                .totalPage(comments.getTotalPages())
                .totalSize((int) comments.getTotalElements())
                .build();
    }

    public Comment createComment(Long userId, Long postId, String content) {
        User user = userService.getUser(userId);
        Post post = postService.findPost(postId);
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

//        if (user.getId() != post.getUser().getId()) {
//            notificationService.sendNotification(user, post.getUser(), NotificationType.COMMENT, postId);
//        }

        return commentRepository.save(comment);
    }

    public boolean deleteComment(Long userId, Long postId, Long commentId) {
        User user = userService.getUser(userId);
        Post post = postService.findPost(postId);
        Comment comment = getComment(postId, commentId);

        if (userId != post.getUser().getId()) {
            if (user.getId() != comment.getUser().getId()) {
                throw new ApiException(Error.NOT_COMMENT_OWNER);
            }
        }

        commentRepository.delete(comment);

        return true;
    }

    public Comment getComment(Long postId, Long commentId) {
        Optional<Comment> optComment = commentRepository.findByIdAndPostId(commentId, postId);
        if (optComment.isEmpty()) throw new ApiException(Error.NOT_EXIST_COMMENT);

        return optComment.get();
    }

    public Comment getCommentById(Long commentId) {
        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (optComment.isEmpty()) throw new ApiException(Error.NOT_EXIST_COMMENT);

        return optComment.get();
    }
}
