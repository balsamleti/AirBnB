package com.airbnb.be.utils;

import com.airbnb.be.api.ApiError;
import com.airbnb.be.api.ApiException;
import com.airbnb.be.modals.Payload;
import com.airbnb.be.modals.RequestParams;
import com.airbnb.be.services.ABLookupService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.SynchronousSink;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.airbnb.be.api.ApiConstants.*;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Slf4j
@UtilityClass
public class ABUtils {

    public static RequestParams getParam(ServerRequest req) {
        log.info("Headers Received: {}", getAllReceivedHeaders(req));
        return RequestParams.builder()
                .startTime(new AtomicReference<>(currentTimeMillis()))
                .method(req.method().name())
                .path(req.path())
                .id(getPathVariable(req, ID))
                .channel(req.headers().firstHeader(CHANNEL))
                .application(req.headers().firstHeader(APPLICATION))
                .identifier(req.headers().firstHeader(IDENTIFIER))
                .transactionId(req.headers().firstHeader(TRANS_ID))
                .idType(ofNullable(getQueryParam(req, ID_TYPE)).orElse(TRANS_ID))
                .systemDateTime(getSysTime())
                .build();
    }

    private static String getAllReceivedHeaders(ServerRequest req) {
        return req.headers().asHttpHeaders().entrySet().stream()
                .filter(e -> StringUtils.isNoneBlank(e.getKey()) && !CollectionUtils.isEmpty(e.getValue()))
                .map(e -> e.getKey().concat(":").concat(e.getValue().get(0)))
                .collect(Collectors.joining("|"));
    }

    public static String getQueryParam(ServerRequest req, String name) {
        return req.queryParam(name).map(String::valueOf).orElse(null);
    }

    private static String getPathVariable(ServerRequest req, String key) {
        try {
            return req.pathVariable(key);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static String getSysTime() {
        return new SimpleDateFormat(DATE_TIME_PATTERN).format(Calendar.getInstance().getTime());
    }

    public static String context(RequestParams p) {
        return context(p, null);
    }

    public static String context(RequestParams p, AtomicReference<Long> startTime) {
        return String.format("[%s|%s|%s|%sms]",
                p.getMethod(), p.getPath(), p.getApplication(),
                isNull(startTime) ? getElapsedTime(p) : currentTimeMillis() - startTime.get());
    }

    public static long getElapsedTime(RequestParams req) {
        return currentTimeMillis() - req.getStartTime().get();
    }

    public static BiConsumer<Payload, SynchronousSink<Payload>> validateApplication(RequestParams rp, ABLookupService service) {
        return (req, sink) -> {
            if (service.isValidApplication(rp.getApplication())) {
                sink.next(req);
            } else {
                sink.error(new ApiException(ApiError.of(10009)));
            }
        };
    }

}