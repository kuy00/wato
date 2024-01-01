package com.wato.watobackend.service;

import com.wato.watobackend.model.Inquiry;
import com.wato.watobackend.model.User;
import com.wato.watobackend.repository.InquiryRepository;
import com.wato.watobackend.request.InquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final UserService userService;
    private final InquiryRepository inquiryRepository;

    public Inquiry createInquiry(Long userId, InquiryRequest request) {
        User user = userService.getUser(userId);
        Inquiry inquiry = Inquiry.builder()
                .email(request.getEmail())
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        return inquiryRepository.save(inquiry);
    }
}
