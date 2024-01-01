package com.wato.watobackend.repository;

import com.wato.watobackend.dto.CommentDto;
import com.wato.watobackend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select new com.wato.watobackend.dto.CommentDto(c.id, c.user.id, c.user.nickname, c.user.imageUrl, c.content, c.createDate, c.updateDate) " +
            "FROM Comment c where c.post.id =:postId order by c.id desc")
    Page<CommentDto> findAllPage(Long postId, PageRequest pageRequest);

    @Query("select c from Comment c where c.id =:commentId and c.post.id =:postId")
    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);
}
