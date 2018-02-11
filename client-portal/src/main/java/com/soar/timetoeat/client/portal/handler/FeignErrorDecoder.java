package com.soar.timetoeat.client.portal.handler;

import com.google.gson.Gson;
import com.soar.timetoeat.util.faults.ClientException;
import com.soar.timetoeat.util.faults.ExceptionResponse;
import feign.Response;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static feign.FeignException.errorStatus;

/**
 * Unfortunately, this is the one silly thing with using Feign.
 * Whenever you encounter a response with an error code, even
 * if you've wrapped it up nicely in a {@link ResponseEntity},
 * by default feign will just throw a {@link feign.FeignException}.
 * While I would have preferred just receiving a {@link ResponseEntity}
 * and checking the error myself, the reason behind this is that
 * "an error is an error and must be treated and handle just
 * like any exception". You win some, and you lose some. ¯\_(ツ)_/¯
 * <p>
 * Here I'm intervening and wrapping errors in the 400 range in a
 * {@link ClientException} class.
 */
public class FeignErrorDecoder implements feign.codec.ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 401) {
            final ExceptionResponse exceptionResponse = new ExceptionResponse("Wrong username or password", null);
            return new ClientException(exceptionResponse);
        } else if (response.status() >= 400 && response.status() <= 499) {
            Gson g = new Gson();
            try {
                final ExceptionResponse exceptionResponse = g.fromJson(response.body().asReader(), ExceptionResponse.class);
                return new ClientException(exceptionResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new RuntimeException(response.reason());
        }
        return errorStatus(methodKey, response);
    }

}
