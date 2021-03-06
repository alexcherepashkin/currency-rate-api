package ua.alexch.currency.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ua.alexch.currency.exception.ApiError;
import ua.alexch.currency.exception.ErrorType;
import ua.alexch.currency.exception.InvalidParameterException;
import ua.alexch.currency.exception.SourceNotFoundException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String INVALID_PARAM_MESSAGE = "Invalid parameter [%s='%s'] supplied";
    public static final boolean LOG_ERROR = true;
    public static final boolean LOG_WARN = false;

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Object> handleInvalidParameter(InvalidParameterException ex, WebRequest request) {
        logEx(ex, request, LOG_ERROR);

        final String message = String.format(INVALID_PARAM_MESSAGE, ex.getParamName(), ex.getParamValue());
        return buildResponse(ErrorType.BAD_REQUEST, message);
    }

    @ExceptionHandler(SourceNotFoundException.class)
    public ResponseEntity<Object> handleSourceNotFound(SourceNotFoundException ex, WebRequest request) {
        logEx(ex, request, LOG_ERROR);

        final String message = String.format(INVALID_PARAM_MESSAGE, ex.getParamName(), ex.getParamValue());
        return buildResponse(ErrorType.NOT_FOUND, message);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logEx(ex, request, LOG_WARN);
        return buildResponse(ErrorType.NOT_FOUND, getMessage(ex));
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logEx(ex, request, LOG_WARN);

        final StringBuilder message = new StringBuilder().append(getMessage(ex));

        Set<HttpMethod> methods = ex.getSupportedHttpMethods();
        if (methods != null) {
            message.append(". Supported methods are:");
            methods.forEach(m -> message.append(" ").append(m));
            headers.setAllow(methods);
        }
        return buildResponse(ErrorType.NOT_SUPPORTED, message.toString());
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logEx(ex, request, LOG_WARN);
        return buildResponse(ErrorType.BAD_REQUEST, getMessage(ex));
    }

    @Override
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logEx(ex, request, LOG_ERROR);
        return buildResponse(ErrorType.BAD_REQUEST, getMessage(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(Exception ex, WebRequest request) {
        logEx(ex, request, LOG_ERROR);
        return buildResponse(ErrorType.APP_ERROR, getMessage(ex));
    }

    private ResponseEntity<Object> buildResponse(ErrorType error, String message) {

        final ApiError response = new ApiError(error, message);
        return ResponseEntity.status(error.getStatus()).body(response);
    }

    private void logEx(Exception ex, WebRequest req, boolean logError) {
        Throwable rootCause = getRootCause(ex);
        if (logError) {
            LOGGER.error(ex.getClass().getSimpleName() + " at request '" + req.getDescription(false) + "' \nCaused by:", rootCause);
        } else {
            LOGGER.warn("{} at request '{}'", rootCause, req.getDescription(false));
        }
    }

    private String getMessage(Exception ex) {
        String message = ex.getLocalizedMessage();
        if (message != null) {
            return message;

        } else {
            Throwable rootCause = getRootCause(ex);
            return (rootCause.getLocalizedMessage() != null)
                    ? rootCause.getLocalizedMessage()
                    : rootCause.getClass().getName();
        }
    }

    private Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
