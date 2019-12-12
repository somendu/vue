package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.data.TmeAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TmeAuthenticationService {

    private final TmeAuthenticationRepository tmeAuthenticationRepository;

    @Cacheable(cacheNames = "lookups", key = "'tmeAuthenticationToken'", sync = true)
    public String getTmeAuthenticationToken() {
        return tmeAuthenticationRepository.getTmeAuthenticationToken().getToken();
    }

}
