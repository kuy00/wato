package com.wato.watobackend.controller;

import com.wato.watobackend.model.Faq;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/faq")
@RestController
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public ApiResponse getFaqs() {
        List<Faq> faqs = faqService.getFaqs();

        return ApiResponse.builder().data(faqs).build();
    }

    @GetMapping("/{id}")
    public ApiResponse getFaq(@PathVariable Long id) {
        Faq faq = faqService.getFaq(id);

        return ApiResponse.builder().data(faq).build();
    }
}
