package com.airbnb.be.handler;

import com.airbnb.be.api.ApiError;
import io.github.cdimascio.openapi.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.airbnb.be.utils.ABUtils.getParam;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileHandler {

//    private final Validate<ApiError> validate;

//    @NotNull
//    public Mono<ServerResponse> getProfile(ServerRequest request) {
//        return validate.request(request, () -> cacheService.get(
//                getParam(request)).flatMap(o -> ok().bodyValue(o)).switchIfEmpty(noContent().build()));
//    }
//
//    @NotNull
//    public Mono<ServerResponse> addGuest(ServerRequest request) {
//        return validate.request(request)
//                .withBody(GettingStarted.class, reqBody -> ok().body(cacheService.savePageOne(
//                        SOPayload.builder().gettingStarted(reqBody).params(getParam(request)).build()), ServiceResponse.class));
//    }


}
