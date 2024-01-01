package com.wato.watobackend.service;

import com.wato.watobackend.dto.EmailAuthDto;
import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.dto.PostDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Block;
import com.wato.watobackend.model.Country;
import com.wato.watobackend.model.User;
import com.wato.watobackend.model.constant.AuthType;
import com.wato.watobackend.model.constant.Role;
import com.wato.watobackend.model.constant.UserStatus;
import com.wato.watobackend.repository.BlockRepository;
import com.wato.watobackend.repository.UserRepository;
import com.wato.watobackend.request.SignUpRequest;
import com.wato.watobackend.request.TargetRequest;
import com.wato.watobackend.request.UserRequest;
import com.wato.watobackend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserService {

    private final NotificationSettingService notificationSettingService;
    private final CountryService countryService;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public PageDto getUsers(Long id, int page, int size) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) throw new ApiException(Error.NOT_EXIST_USER);
        if (optUser.get().getRole().toString() != "ADMIN") throw new ApiException(Error.NO_PERMISSION);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.getAll(pageRequest);

        return PageDto.builder()
                .list(users.getContent())
                .page(users.getPageable().getPageNumber() + 1)
                .size(users.getPageable().getPageSize())
                .totalPage(users.getTotalPages())
                .totalSize((int) users.getTotalElements())
                .build();
    }

    public User createUser(SignUpRequest request) {
        Optional<User> optUsedEmailUser = userRepository.findByEmail(request.getEmail());
        if (optUsedEmailUser.isPresent()) throw new ApiException(Error.EMAIL_ALREADY_USED);

        if (StringUtils.hasText(request.getNickname())) {
            checkNicknameLength(request.getNickname());

            Optional<User> optUsedNicknameUser = userRepository.findByNickname(request.getNickname());
            if (optUsedNicknameUser.isPresent()) throw new ApiException(Error.NICKNAME_ALREADY_USED);
        }

        User user = User.builder()
                .authType(AuthType.EMAIL)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .gender(request.getGender())
                .job(request.getJob())
                .yearOfBirth(request.getYearOfBirth())
                .role(Role.USER)
                .status(UserStatus.NORMALLY)
                .build();

        if (request.getCountry() != null) {
            Country country = countryService.getCountry(request.getCountry());
            user.setCountry(country);
        }

        user = userRepository.save(user);

        return user;
    }

    public User getUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) throw new ApiException(Error.NOT_EXIST_USER);

        if (StringUtils.hasText(optUser.get().getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(optUser.get().getImageUrl()).toUriString();
            optUser.get().setProfileImageUrl(imageUrl);
        }

        return optUser.get();
    }

    public User updateProfile(Long id, UserRequest request, MultipartFile file) {
        User user = getUser(id);
        if (StringUtils.hasText(request.getNickname())) {

            if (StringUtils.hasText(user.getNickname())) {
                if (!user.getNickname().equals(request.getNickname())) {

                    checkNicknameLength(request.getNickname());

                    Optional<User> optUser = userRepository.findByNickname(request.getNickname());
                    if (optUser.isPresent()) throw new ApiException(Error.NICKNAME_ALREADY_USED);
                }
            } else {
                checkNicknameLength(request.getNickname());

                Optional<User> optUser = userRepository.findByNickname(request.getNickname());
                if (optUser.isPresent()) throw new ApiException(Error.NICKNAME_ALREADY_USED);
            }

            user.setNickname(request.getNickname());
        }

        if (request.getCountry() != null) {
            Country country = countryService.getCountry(request.getCountry());
            user.setCountry(country);
        }
        user.setGender(request.getGender());
        user.setYearOfBirth(request.getYearOfBirth());
        user.setJob(request.getJob());

        try {
            if (file != null && !file.isEmpty()) {
                Path path = Paths.get("images/user/" + id).toAbsolutePath().normalize();
                if (!Files.exists(path)) Files.createDirectory(path);
                String fileName = "profile." + file.getOriginalFilename().split("\\.")[1];
                Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                user.setImageUrl("/images/user/" + id + "/" + fileName);
            }
        } catch (Exception e) {
            log.error("updateUser imageUpload message: {}, id: {}", e.getMessage(), id);
            throw new ApiException(Error.IMAGE_UPLOAD);
        }

        user = userRepository.save(user);
        if (StringUtils.hasText(user.getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(user.getImageUrl()).toUriString();
            user.setProfileImageUrl(imageUrl);
        }

        return user;
    }

    public User signUp(String authKey, SignUpRequest request) {
        EmailAuthDto emailAuthDto = jwtProvider.getEmailAuthentication(authKey);

        if (!String.valueOf(emailAuthDto.getEmail()).equals(String.valueOf(request.getEmail()))) throw new ApiException(Error.AUTH_FAILED);

        User user = createUser(request);
        notificationSettingService.createNotificationSetting(user.getId());

        return user;
    }

    public boolean checkEmail(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);

        return optUser.isPresent() ? false : true;
    }

    public boolean checkNicknameBySignup(String nickname) {
        checkNicknameLength(nickname);

        Optional<User> optUser = userRepository.findByNickname(nickname);
        if (optUser.isPresent()) return false;

        return true;
    }

    public boolean checkNicknameByProfile(Long id, String nickname) {
        User user = getUser(id);
        if (StringUtils.hasText(user.getNickname())) {
            if (!user.getNickname().equals(nickname)) {
                checkNicknameLength(nickname);

                Optional<User> optUser = userRepository.findByNickname(nickname);
                if (optUser.isPresent()) return true;
                return false;
            }
        } else {
            checkNicknameLength(nickname);

            Optional<User> optUser = userRepository.findByNickname(nickname);
            if (optUser.isPresent()) return true;
            return false;
        }

        return true;
    }

    private static void checkNicknameLength(String nickname) {
        if (nickname.length() > 6) throw new ApiException(Error.NICKNAME_LENGTH_EXCEEDED);
    }

    public boolean blockUser(Long userId, TargetRequest request) {
        User blocker = getUser(userId);
        User blocked = getUser(request.getUserId());
        if (blocker.getId() == blocked.getId()) throw new ApiException(Error.USER_SELF_BLOCK);

        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .createDate(LocalDateTime.now())
                .build();
        blockRepository.save(block);

        return true;
    }

    public boolean deleteBlockUser(Long userId, TargetRequest request) {
        Optional<Block> optBlock = blockRepository.findByBlockerIdAndBlockedId(userId, request.getUserId());
        if (optBlock.isPresent()) {
            blockRepository.delete(optBlock.get());
        }

        return true;
    }
}
