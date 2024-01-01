package com.wato.watobackend.controller;

import com.wato.watobackend.model.Inquiry;
import com.wato.watobackend.request.InquiryRequest;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/inquiry")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ApiResponse createInquiry(
            Authentication authentication,
            @RequestBody InquiryRequest request
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Inquiry inquiry = inquiryService.createInquiry(Long.parseLong(userDetails.getUsername()), request);

        return ApiResponse.builder().data(inquiry).build();
    }
}
