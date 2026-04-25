package com.airbnb.be.services;

import com.airbnb.be.generated.GenericResponse;
import com.airbnb.be.generated.users.User;
import com.airbnb.be.modals.Payload;
import com.airbnb.be.modals.RequestParams;
import com.airbnb.be.modals.UsersDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.airbnb.be.api.ApiConstants.*;
import static com.airbnb.be.mapper.GenericResponseMapper.GENERIC_RESPONSE_MAPPER;
import static com.airbnb.be.mapper.UsersMapper.USER_MAPPER;
import static com.airbnb.be.utils.ABUtils.context;
import static com.airbnb.be.utils.ABUtils.validateApplication;
import static java.util.Objects.nonNull;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static reactor.core.publisher.Mono.just;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final ObjectMapper objectMapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final ABLookupService lookupService;

    public Mono<GenericResponse> createUserDetails(Payload payload) {
        var params = payload.getParams();
        return just(payload)
                .doOnNext(p -> log.info(ABM_API_REQ_LOGGER, context(params), params))
                .handle(validateApplication(params, lookupService))
                .flatMap(this::saveData)
                .map(GENERIC_RESPONSE_MAPPER::map)
                .doOnError(x -> log.info(ABM_API_ERR_LOGGER, context(params), x.getMessage()))
                .doFinally(x -> log.info(ABM_API_RES_LOGGER, context(params)));
    }

    public Mono<User> deleteUserDetails(Payload p) {
        var p0 = new Payload();
        var user = new User();
        var params = p.getParams();

        user.setDeleted(true);
        p0.setUser(user);
        p0.setParams(p.getParams());

        return just(p)
                .doOnNext(o -> log.info(ABM_API_REQ_LOGGER, context(params), params))
                .handle(validateApplication(params, lookupService))
                .map(this::findUserById)
                .then(updateUserDetails(p0))
                .doOnError(x -> log.info(ABM_API_ERR_LOGGER, context(params), x.getMessage()))
                .doFinally(x -> log.info(ABM_API_RES_LOGGER, context(params)));
    }

    public Mono<User> updateUserDetails(Payload p) {
        var params = p.getParams();
        return just(p)
                .doOnNext(o -> log.info(ABM_API_REQ_LOGGER, context(params), params))
                .handle(validateApplication(params, lookupService))
                .map(USER_MAPPER::forUpdate)
                .map(this::getUserDetailsAsMap)
                .flatMap(map -> this.updateUser(p, map))
                .map(USER_MAPPER::map)
                .doOnError(x -> log.info(ABM_API_ERR_LOGGER, context(params), x.getMessage()))
                .doFinally(x -> log.info(ABM_API_RES_LOGGER, context(params)));
    }

    public Mono<User> getUserDetails(Payload payload) {
        var params = payload.getParams();
        return just(payload)
                .doOnNext(p -> log.info(ABM_API_REQ_LOGGER, context(params), params))
                .handle(validateApplication(params, lookupService))
                .flatMap(this::findUserById)
                .map(USER_MAPPER::map)
                .doOnError(x -> log.info(ABM_API_ERR_LOGGER, context(params), x.getMessage()))
                .doFinally(x -> log.info(ABM_API_RES_LOGGER, context(params)));
    }

    private Mono<UsersDocument> saveData(Payload p) {
        var startTime = new AtomicReference<Long>();
        return reactiveMongoTemplate.save(USER_MAPPER.map(p))
                .doOnSubscribe(x -> startTime.set(System.currentTimeMillis()))
                .doOnError(ex -> log.error(MONGO_DB_ERR_LOGGER, context(p.getParams()), ex.getMessage()))
                .doFinally(msg -> log.info(MONGO_DB_SEARCH_LOGGER, context(p.getParams(), startTime)));
    }

    private Mono<UsersDocument> findUserById(Payload p) {
        var startTime = new AtomicReference<Long>();
        return reactiveMongoTemplate.findById(p.getParams().getId(), UsersDocument.class)
                .doOnSubscribe(x -> startTime.set(System.currentTimeMillis()))
                .doOnError(ex -> log.error(MONGO_DB_ERR_LOGGER, context(p.getParams()), ex.getMessage()))
                .doFinally(msg -> log.info(MONGO_DB_SEARCH_LOGGER, context(p.getParams(), startTime)));
    }

    private Mono<UsersDocument> updateUser(Payload p, Map<String, Object> map) {
        var params = p.getParams();
        var query = queryWithoutHistory(where(USER_ID).is(params.getId()), params);
        var updateQuery = getUpdateQuery(map, null);
        return findAndUpdateUser(params, query, updateQuery);
    }

    private Mono<UsersDocument> findAndUpdateUser(RequestParams params, Query query, Update updateQuery) {
        var startTime = new AtomicReference<Long>();
        return reactiveMongoTemplate.update(UsersDocument.class)
                .matching(query).apply(updateQuery).withOptions(options().returnNew(true)).findAndModify()
                .doOnSubscribe(x -> startTime.set(System.currentTimeMillis()))
                .doOnError(ex -> log.error(MONGO_DB_ERR_LOGGER, context(params), ex.getMessage()))
                .doFinally(msg -> log.info(MONGO_DB_SEARCH_LOGGER, context(params, startTime)));
    }

    private Update getUpdateQuery(Map<String, Object> map, @Nullable String userId) {
        var update = new Update();
        map.forEach(update::set);
        update.currentDate(UPDATED_DATE);
        map.put(UPDATED_DATE, new Date());
        map.put(UPDATED_BY, APP_NAME);
        if (nonNull(userId)) {
            map.put(USER_ID, userId);
        }
        update.push(REVISION_HISTORY, map);
        return update;
    }

    private Map<String, Object> getUserDetailsAsMap(UsersDocument user) {
        return objectMapper.copy().setSerializationInclusion(JsonInclude.Include.NON_NULL).convertValue(user, Map.class);
    }

    private Query queryWithoutHistory(Criteria criteria, RequestParams params) {
        var query = query(criteria.and(DELETED).in(null, false));
        query.fields().exclude(REVISION_HISTORY);
        log.info("{}: MongoDB Search critria:{}", context(params));
        return query;
    }

}