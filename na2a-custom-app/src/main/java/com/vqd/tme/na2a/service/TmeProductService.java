package com.vqd.tme.na2a.service;

import com.vqd.tme.na2a.data.TmeProductRepository;
import com.vqd.tme.na2a.model.tme.TmePart;
import com.vqd.tme.na2a.model.tme.TmePartsWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TmeProductService {

    private final TmeProductRepository tmeProductRepository;

    public List<TmePart> searchParts(String partNumber, String partName) {
        TmePartsWrapper resultWrapper = tmeProductRepository.searchParts(partNumber);
        return resultWrapper.getParts().stream()
                .filter(tmePart -> partName == null || tmePart.getPartName().startsWith(partName))
                .collect(Collectors.toList());
    }

}
