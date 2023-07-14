package net.example.spring.web;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                 HttpHeaders headers,
                                 HttpStatusCode status,
                                 WebRequest request) {
        Object[] arguments = ex.getDetailMessageArguments();
        if (arguments != null) {
            ProblemDetail body = ex.getBody();
            String detail = body.getDetail();
            detail = (detail == null) ? "" : detail + " ";
            detail += flatStream(arguments).map(Object::toString)
                                           .collect(Collectors.joining(", "));
            body.setDetail(detail);
        }
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    private static Stream<Object> flatStream(Object obj) {
        if (obj == null) {
            return Stream.empty();
        }
        if (obj.getClass().isArray()
                && !obj.getClass().getComponentType().isPrimitive()) {
            return Stream.of((Object[]) obj).flatMap(ErrorResponseHandler::flatStream);
        }
        if (obj instanceof Collection<?> col) {
            return col.stream().flatMap(ErrorResponseHandler::flatStream);
        }
        return Stream.of(obj);
    }

}
