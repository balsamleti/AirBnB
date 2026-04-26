package com.airbnb.be.handler;

import com.airbnb.be.api.ApiError;
import com.airbnb.be.generated.users.User;
import com.airbnb.be.modals.Payload;
import com.airbnb.be.services.UsersService;
import io.github.cdimascio.openapi.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.airbnb.be.utils.ABUtils.getParam;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileHandler {

    private final Validate<ApiError> validate;
    private final UsersService service;

    @NotNull
    public Mono<ServerResponse> getUser(ServerRequest request) {
        return validate.request(request,
                () -> service.getUserDetails(Payload.builder().params(getParam(request)).build())
                        .flatMap(o -> ok().bodyValue(o)).switchIfEmpty(noContent().build()));
    }

    @NotNull
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return validate.request(request).withBody(User.class, reqBody ->
                service.createUserDetails(Payload.builder().user(reqBody).params(getParam(request)).build())
                        .flatMap(body -> status(CREATED).bodyValue(body))
                        .onErrorResume(DuplicateKeyException.class,
                                ex -> status(CONFLICT).bodyValue(ApiError.of(CONFLICT, ex.getMessage()))));
    }

    @NotNull
    public Mono<ServerResponse> patchUser(ServerRequest request) {
        return validate.request(request)
                .withBody(User.class, reqBody ->
                        service.updateUserDetails(Payload.builder().user(reqBody).params(getParam(request)).build())
                                .flatMap(o -> ok().bodyValue(o)).switchIfEmpty(noContent().build()));
    }

    @NotNull
    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        return validate.request(request,
                () -> service.deleteUserDetails(Payload.builder().params(getParam(request)).build())
                        .flatMap(o -> ok().bodyValue(o)).switchIfEmpty(noContent().build()));
    }


}
