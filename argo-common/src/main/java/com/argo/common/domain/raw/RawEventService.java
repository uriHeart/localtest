package com.argo.common.domain.raw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawEventService {
    @Autowired
    private RawEventRepository rawEventRepository;

    public void save(RawEvent rawEvent) {
        rawEventRepository.save(rawEvent);
    }
}
