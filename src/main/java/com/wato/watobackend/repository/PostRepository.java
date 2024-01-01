package com.wato.watobackend.repository;

import com.wato.watobackend.dto.PostDto;
import com.wato.watobackend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select new com.wato.watobackend.dto.PostDto(" +
            "p.id, p.country.id, p.country.name, p.category.id, p.category.name, p.title, p.content, p.user.id,p.user.nickname, p.user.imageUrl, p.imageUrl, size(p.comments), p.createDate, p.updateDate, p.status" +
            ") from Post p where p.id =:postId")
    Optional<PostDto> findDtoById(Long postId);

    @Query("select new com.wato.watobackend.dto.PostDto(" +
            "p.id, p.country.id, p.country.name, p.category.id, p.category.name, p.title, p.content, p.user.id,p.user.nickname, p.user.imageUrl, p.imageUrl, size(p.comments), p.createDate, p.updateDate, p.status" +
            ") from Post p where p.status = 0 order by p.id desc")
    Page<PostDto> findActivityAllPage(PageRequest pageRequest);

    @Query("select new com.wato.watobackend.dto.PostDto(" +
            "p.id, p.country.id, p.country.name, p.category.id, p.category.name, p.title, p.content, p.user.id,p.user.nickname, p.user.imageUrl, p.imageUrl, size(p.comments), p.createDate, p.updateDate, p.status" +
            ") from Post p where p.status = 0 order by size(p.comments) desc , p.id desc")
    Page<PostDto> findActivityAllPopularityPage(PageRequest pageRequest);

    @Query(value = "select p.category_id from post p where p.status = 0 group by p.category_id order by count(p.category_id) desc limit 1", nativeQuery = true)
    Long findActivityCategoryByTopCount();

    @Query("select new com.wato.watobackend.dto.PostDto(" +
            "p.id, p.country.id, p.country.name, p.category.id, p.category.name, p.title, p.content, p.user.id,p.user.nickname, p.user.imageUrl, p.imageUrl, size(p.comments), p.createDate, p.updateDate, p.status" +
            ") from Post p where p.status = 0 and p.category.id =:topCategoryId order by size(p.comments) desc , p.id desc")
    Page<PostDto> findActivityByCategory(Long topCategoryId, PageRequest pageRequest);

    @Query(value = "select p.country_id from post p where p.status = 0 group by p.country_id order by count(p.country_id) desc limit 1", nativeQuery = true)
    Long findActivityCountryByTopCount();

    @Query("select new com.wato.watobackend.dto.PostDto(" +
            "p.id, p.country.id, p.country.name, p.category.id, p.category.name, p.title, p.content, p.user.id,p.user.nickname, p.user.imageUrl, p.imageUrl, size(p.comments), p.createDate, p.updateDate, p.status" +
            ") from Post p where p.status = 0 and p.country.id =:topCategoryId order by size(p.comments) desc , p.id desc")
    Page<PostDto> findActivityByCountry(Long topCategoryId, PageRequest pageRequest);
}
