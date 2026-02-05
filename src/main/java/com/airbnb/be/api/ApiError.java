package com.airbnb.be.api;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;

import static com.airbnb.be.services.ABLookupService.errMsg;
import static com.airbnb.be.utils.ABErrorUtils.parseError;

@Data
@Generated
@NoArgsConstructor
public class ApiError {

    private HttpStatusCode status;
    private Integer code;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatusCode status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatusCode status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ApiError(ServerRequest request, HttpStatusCode status, List<String> errors) {
        this.status = status;
        this.code = parseError(errors.get(0));
        this.message = errMsg(this.code);
        this.errors = errors;
    }

    public static ApiError of(HttpStatusCode status, String message) {
        return new ApiError(status, message);
    }

    public static ApiError of(HttpStatusCode status, int code) {
        return new ApiError(status, code, errMsg(code));
    }

    public static ApiError of(int code) {
        return new ApiError(HttpStatus.BAD_REQUEST, code, errMsg(code));
    }

}
