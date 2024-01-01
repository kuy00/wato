package com.wato.watobackend.service;

import com.wato.watobackend.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlockService {

    private final BlockRepository blockRepository;
}
