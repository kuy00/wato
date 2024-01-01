package com.wato.watobackend.service;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Faq;
import com.wato.watobackend.repository.FaqRepository;
import com.wato.watobackend.request.FaqRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public List<Faq> getFaqs() {
        return faqRepository.findAll();
    }

    public Faq getFaq(Long id) {
       Optional<Faq> optFaq = faqRepository.findById(id);
       if (optFaq.isEmpty()) throw new ApiException(Error.NOT_EXIST_FAQ);

       return optFaq.get();
    }

    public Faq createFaq(FaqRequest request) {
        Faq faq = Faq.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return faqRepository.save(faq);
    }
}
