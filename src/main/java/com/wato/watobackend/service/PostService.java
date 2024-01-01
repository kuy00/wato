package com.wato.watobackend.service;

import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.dto.PostDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.*;
import com.wato.watobackend.model.constant.PostStatus;
import com.wato.watobackend.repository.PostRepository;
import com.wato.watobackend.repository.ReportRepository;
import com.wato.watobackend.request.PostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final CountryService countryService;
    private final CategoryService categoryService;
    private final ReportRepository reportRepository;

    public PageDto getPosts(String filter, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostDto> posts;
        if (filter.equals("home")) {
            posts = postRepository.findActivityAllPage(pageRequest);
        } else {
            posts = postRepository.findActivityAllPopularityPage(pageRequest);
        }

        return PageDto.builder()
                .list(posts.getContent())
                .page(posts.getPageable().getPageNumber() + 1)
                .size(posts.getPageable().getPageSize())
                .totalPage(posts.getTotalPages())
                .totalSize((int) posts.getTotalElements())
                .build();
    }

    public PostDto getPost(Long postId) {
        Optional<PostDto> optPost = postRepository.findDtoById(postId);
        if (optPost.isEmpty()) throw new ApiException(Error.NOT_EXIST_POST);

        if (StringUtils.hasText(optPost.get().getUserImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(optPost.get().getUserImageUrl()).toUriString();
            optPost.get().setProfileImageUrl(imageUrl);
        }

        if (StringUtils.hasText(optPost.get().getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(optPost.get().getImageUrl()).toUriString();
            optPost.get().setPostImageUrl(imageUrl);
        }


        return optPost.get();
    }

    public Post findPost(Long postId) {
        Optional<Post> optPost = postRepository.findById(postId);
        if (optPost.isEmpty()) throw new ApiException(Error.NOT_EXIST_POST);

        return optPost.get();
    }

    public Post createPost(Long userId, PostRequest request, MultipartFile file) {
        if (!StringUtils.hasText(request.getTitle())) throw new ApiException(Error.EMPTY_DATA);
        if (!StringUtils.hasText(request.getContent())) throw new ApiException(Error.EMPTY_DATA);
        if (request.getCountry() == null) throw new ApiException(Error.EMPTY_DATA);
        if (request.getCategory() == null) throw new ApiException(Error.EMPTY_DATA);
        Country country = countryService.getCountry(request.getCountry());
        Category category = categoryService.getCategory(request.getCategory());

        User user = userService.getUser(userId);
        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .country(country)
                .category(category)
                .build();
        post = postRepository.save(post);

        try {
            if (file != null && !file.isEmpty()) {
                Path path = Paths.get("images/post/" + post.getId()).toAbsolutePath().normalize();
                if (!Files.exists(path)) Files.createDirectory(path);
                String fileName = "post." + file.getOriginalFilename().split("\\.")[1];
                Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                post.setImageUrl("/images/post/" + post.getId() + "/" + fileName);
                post = postRepository.save(post);
            }
        } catch (Exception e) {
            log.error("createPost imageUpload message: {}, id: {}", e.getMessage(), post.getId());
            throw new ApiException(Error.IMAGE_UPLOAD);
        }

        if (StringUtils.hasText(post.getUser().getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(post.getUser().getImageUrl()).toUriString();
            post.getUser().setProfileImageUrl(imageUrl);
        }

        if (StringUtils.hasText(post.getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(post.getImageUrl()).toUriString();
            post.setPostImageUrl(imageUrl);
        }

        return post;
    }

    public Post updatePost(Long userId, Long postId, PostRequest request, MultipartFile file) {
        User user = userService.getUser(userId);
        Post post = findPost(postId);
        if (post.getStatus() == PostStatus.DELETED) throw new ApiException(Error.DELETED_POST);
        if (user.getId() != post.getUser().getId()) throw new ApiException(Error.NOT_POST_OWNER);
        if (request.getCountry() != null) {
            Country country = countryService.getCountry(request.getCountry());
            post.setCountry(country);
        }

        if (request.getCategory() != null) {
            Category category = categoryService.getCategory(request.getCategory());
            post.setCategory(category);
        }
        if (StringUtils.hasText(request.getTitle())) {
            post.setTitle(request.getTitle());
        }

        if (StringUtils.hasText(request.getContent())) {
            post.setContent(request.getContent());
        }

        try {
            if (file != null && !file.isEmpty()) {
                Path path = Paths.get("images/post/" + post.getId()).toAbsolutePath().normalize();
                if (!Files.exists(path)) Files.createDirectory(path);
                String fileName = "post." + file.getOriginalFilename().split("\\.")[1];
                Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                post.setImageUrl("/images/post/" + post.getId() + "/" + fileName);
            }
        } catch (Exception e) {
            log.error("createPost imageUpload message: {}, id: {}", e.getMessage(), post.getId());
            throw new ApiException(Error.IMAGE_UPLOAD);
        }

        post = postRepository.save(post);
        if (StringUtils.hasText(post.getUser().getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(post.getUser().getImageUrl()).toUriString();
            post.getUser().setProfileImageUrl(imageUrl);
        }

        if (StringUtils.hasText(post.getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(post.getImageUrl()).toUriString();
            post.setPostImageUrl(imageUrl);
        }

        return post;
    }

    public boolean deletePost(Long userId, Long postId) {
        User user = userService.getUser(userId);
        Post post = findPost(postId);
        if (user.getId() != post.getUser().getId()) throw new ApiException(Error.NOT_POST_OWNER);

        post.setStatus(PostStatus.DELETED);
        postRepository.save(post);

        return true;
    }

    public boolean postReport(Long userId, Long postId, String reason) {
        User user = userService.getUser(userId);
        Post post = findPost(postId);
        if (user.getId() == post.getUser().getId()) throw new ApiException(Error.POST_OWNER);

        Report report = Report.builder()
                .reporter(user)
                .post(post)
                .reason(reason)
                .createDate(LocalDateTime.now())
                .build();
        reportRepository.save(report);

        return true;
    }

    public PageDto getRecommendByCategory(int page, int size) {
        Long topCategoryId = postRepository.findActivityCategoryByTopCount();

        log.info("recommend category: {}", topCategoryId);
        Page<PostDto> posts = postRepository.findActivityByCategory(topCategoryId, PageRequest.of(page, size));

        return PageDto.builder()
                .list(posts.getContent())
                .page(posts.getPageable().getPageNumber() + 1)
                .size(posts.getPageable().getPageSize())
                .totalPage(posts.getTotalPages())
                .totalSize((int) posts.getTotalElements())
                .build();
    }

    public PageDto getRecommendByCountry(int page, int size) {
        Long topCountryId = postRepository.findActivityCountryByTopCount();
        log.info("recommend country: {}", topCountryId);
        Page<PostDto> posts = postRepository.findActivityByCountry(topCountryId, PageRequest.of(page, size));

        return PageDto.builder()
                .list(posts.getContent())
                .page(posts.getPageable().getPageNumber() + 1)
                .size(posts.getPageable().getPageSize())
                .totalPage(posts.getTotalPages())
                .totalSize((int) posts.getTotalElements())
                .build();
    }
}
