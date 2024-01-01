package com.wato.watobackend.service;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Country;
import com.wato.watobackend.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    public Country getCountry(Long id) {
        Optional<Country> optCountry = countryRepository.findById(id);
        if (optCountry.isEmpty()) throw new ApiException(Error.NOT_EXIST_COUNTRY);

        return optCountry.get();
    }
}
