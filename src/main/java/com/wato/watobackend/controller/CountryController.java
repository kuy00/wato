package com.wato.watobackend.controller;

import com.wato.watobackend.model.Country;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/country")
@RestController
public class CountryController {

    private final CountryService countryService;


    @GetMapping
    public ApiResponse getCountries() {
        List<Country> countries = countryService.getCountries();

        return ApiResponse.builder().data(countries).build();
    }
}
