package com.airbnb.be.services;

import com.airbnb.be.generated.GenericResponse;
import com.airbnb.be.generated.users.User;
import com.airbnb.be.mapper.GenericResponseMapper;
import com.airbnb.be.mapper.UsersMapper;
import com.airbnb.be.modals.Payload;
import com.airbnb.be.modals.UsersDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static com.airbnb.be.api.ApiConstants.*;
import static com.airbnb.be.utils.ABUtils.context;
import static com.airbnb.be.utils.ABUtils.validateApplication;
import static reactor.core.publisher.Mono.just;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final ABLookupService lookupService;
    private final GenericResponseMapper mapper;
    private final UsersMapper usersMapper;

    public Mono<GenericResponse> createUserDetails(Payload payload) {
        var params = payload.getParams();
        return just(payload)
                .doOnNext(p -> log.info(ABM_API_REQ_LOGGER, context(params), params))
                .handle(validateApplication(params, lookupService))
                .flatMap(this::saveData)
                .map(mapper::map)
                .doOnError(x -> log.info(ABM_API_ERR_LOGGER, context(params), x.getMessage()))
                .doFinally(x -> log.info(ABM_API_RES_LOGGER, context(params)));
    }

    public Mono<User> getUserDetails(Payload payload) {
        var params = payload.getParams();
        return just(payload)
                .doOnNext(p -> log.info(ABM_API_REQ_LOGGER, context(params), params))
                .handle(validateApplication(params, lookupService))
                .flatMap(this::fetchData)
                .map(usersMapper::map)
                .doOnError(x -> log.info(ABM_API_ERR_LOGGER, context(params), x.getMessage()))
                .doFinally(x -> log.info(ABM_API_RES_LOGGER, context(params)));
    }

    private Mono<UsersDocument> saveData(Payload p) {
        var startTime = new AtomicReference<Long>();
        return reactiveMongoTemplate.save(usersMapper.map(p))
                .doOnSubscribe(x -> startTime.set(System.currentTimeMillis()))
                .doOnError(ex -> log.error(MONGO_DB_ERR_LOGGER, context(p.getParams()), ex.getMessage()))
                .doFinally(msg -> log.info(MONGO_DB_SEARCH_LOGGER, context(p.getParams(), startTime)));
    }

    private Mono<UsersDocument> fetchData(Payload p) {
        var startTime = new AtomicReference<Long>();
        return reactiveMongoTemplate.findById(p.getParams().getId(), UsersDocument.class)
                .doOnSubscribe(x -> startTime.set(System.currentTimeMillis()))
                .doOnError(ex -> log.error(MONGO_DB_ERR_LOGGER, context(p.getParams()), ex.getMessage()))
                .doFinally(msg -> log.info(MONGO_DB_SEARCH_LOGGER, context(p.getParams(), startTime)));
    }

}