package com.airbnb.be.routes;

import com.airbnb.be.handler.UserProfileHandler;
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

    @Bean
    public RouterFunction<ServerResponse> user(UserProfileHandler handler) {
        return route()
                .GET(basePath.concat("/users/profile/{Id}"), handler::getUser)
                .POST(basePath.concat("/users/profile"), handler::createUser)
                .PATCH(basePath.concat("/users/profile/{Id}"), handler::patchUser)
                .DELETE(basePath.concat("/users/profile/{Id}"), handler::deleteUser)
                .build();
    }

//    @Bean
//    RouterFunction<ServerResponse> guest() {
//        return route()
//                .GET("/users/guests", handler::getProfile)
//                .POST("/users/guests", )
//                .PATCH("/users/guests/{guestId}", handler::updateProfile)
//                .DELETE("/users/guests/{guestId}", )
//                .build();
//    }


}
