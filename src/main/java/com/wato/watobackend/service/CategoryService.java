package com.wato.watobackend.service;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Category;
import com.wato.watobackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        Optional<Category> optCategory = categoryRepository.findById(id);
        if (optCategory.isEmpty()) throw new ApiException(Error.NOT_EXIST_CATEGORY);

        return optCategory.get();
    }
}
