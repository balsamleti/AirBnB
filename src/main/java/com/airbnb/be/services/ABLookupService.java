package com.airbnb.be.services;

import com.airbnb.be.modals.LookUpDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.airbnb.be.api.ApiConstants.*;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ABLookupService {
    private final ReactiveMongoTemplate lookupMongoTemplate;

    private static final Map<Integer, String> errorMap = new HashMap<>();
    private static final Set<String> applicationList = new HashSet<>();

    @Scheduled(fixedRate = 90000L)
    public void init() {
        initializeApplicationList();
        initializeErrorList();
    }

    public static String errMsg(int code) {
        return errorMap.get(code);
    }

    public boolean isValidApplication(String app) {
        return applicationList.contains(app);
    }

    protected void initializeApplicationList() {
        lookupMongoTemplate.findById(APPLICATIONS, LookUpDocument.class).map(LookUpDocument::getData).flatMapIterable(Map::entrySet).doFinally(msg -> log.info("application list ".concat(DATA_INIT_COMPLETE))).subscribe(entry -> applicationList.add(entry.getKey()), error -> log.error("application list ".concat(DATA_INIT_FAILURE)));
    }

    protected void initializeErrorList() {
        lookupMongoTemplate.findById(ERRORS, LookUpDocument.class).map(LookUpDocument::getData).map(m -> m.entrySet().stream().collect(toMap(e -> parseInt(e.getKey()), e -> e.getValue().toString()))).doFinally(msg -> log.info("error list ".concat(DATA_INIT_COMPLETE))).subscribe(errorMap::putAll, error -> log.error("application list ".concat(DATA_INIT_FAILURE)));
    }

}