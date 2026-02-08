package com.airbnb.be.modals;

import lombok.*;

import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class RequestParams {

    private AtomicReference<Long> startTime;
    private String channel;
    private String application;
    private String identifier;
    private String transactionId;
    private String id;
    private String idType;
    private String systemDateTime;
    private String method;
    private String path;

}