package com.airbnb.be.routes;

import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.airbnb.be.api.ApiConstants.*;
import static java.net.URI.create;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.temporaryRedirect;

@Generated
@Configuration
public class ABRoutes {

    @Value("${app.base-path}")
    private String basePath;

    @Value("${app.service-name}")
    private String appName;

    @Bean
    public RouterFunction<ServerResponse> apiRoute() {
        return route()
                .GET("/", req -> temporaryRedirect(create("/index.html")).build())
                .GET(basePath.concat(PING), req -> ok().bodyValue(appName.concat(PING_RESPONSE)))
                .GET(basePath.concat(HEALTH), req -> temporaryRedirect(create(HEALTH_URL_REDIRECT)).build())
                .build();
    }

//    @Bean
//    RouterFunction<ServerResponse> userRoutes(UserProfileHandler handler) {
//        return route()
//                .GET("/users/profile", handler::getProfile)
//                .PATCH("/users/profile", handler::updateProfile)
//                .GET("/users/myBookings", handler::getMyBookings)
//                .GET("/users/guests", handler::getGuests)
//                .POST("/users/guests", handler::addGuest)
//                .PUT("/users/guests/{guestId}", handler::updateGuest)
//                .DELETE("/users/guests/{guestId}", handler::deleteGuest)
//                .build();
//    }


}
