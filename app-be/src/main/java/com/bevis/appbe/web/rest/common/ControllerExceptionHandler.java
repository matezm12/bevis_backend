package com.bevis.appbe.web.rest.common;

import com.bevis.appbe.web.rest.vm.FieldErrorVM;
import com.bevis.asset.DynamicAssetException;
import com.bevis.balance.exception.CryptoBalanceException;
import com.bevis.account.exception.PasswordNotMatchException;
import com.bevis.user.authorization.exception.UserNotActivatedException;
import com.bevis.credits.exception.DuplicateCreditsChargeException;
import com.bevis.common.exception.BaseException;
import com.bevis.common.exception.DuplicateObjectException;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.versioning.exception.VersionException;
import com.bevis.exchange.exception.ExchangeException;
import com.bevis.appbe.web.rest.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({DuplicateCreditsChargeException.class})
    ResponseEntity<ErrorResponse> duplicateCreditsChargeExceptionExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                buildErrorResponse("DuplicateCreditsChargeException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({DuplicateObjectException.class})
    ResponseEntity<ErrorResponse> duplicateObjectExceptionExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                buildErrorResponse("DuplicateObjectException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    ResponseEntity<ErrorResponse> objectNotFoundExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                buildErrorResponse("ObjectNotFoundException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({CryptoBalanceException.class})
    ResponseEntity<ErrorResponse> cryptoBalanceExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                buildErrorResponse("CryptoBalanceException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({ExchangeException.class})
    ResponseEntity<ErrorResponse> exchangeExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                buildErrorResponse("ExchangeException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({UserNotActivatedException.class})
    ResponseEntity<ErrorResponse> userNotActivatedExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                buildErrorResponse("UserNotActivatedException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({PasswordNotMatchException.class})
    ResponseEntity<ErrorResponse> passwordNotMatchExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                buildErrorResponse("PasswordNotMatchException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({VersionException.class})
    ResponseEntity<ErrorResponse> versionExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UPGRADE_REQUIRED;
        return new ResponseEntity<>(
                buildErrorResponse("VersionException", httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({BaseException.class, MissingServletRequestParameterException.class, DynamicAssetException.class})
    ResponseEntity<ErrorResponse> baseExceptionHandler(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                buildErrorResponse(ex.getClass().getSimpleName(), httpStatus, ex, request),
                new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldErrorVM(f.getObjectName().replaceFirst("DTO$", ""), f.getField(), f.getCode()))
                .collect(Collectors.toList());

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .error(httpStatus.getReasonPhrase())
                        .path(request.getRequestURI())
                        .type(ex.getClass().getSimpleName())
                        .status(httpStatus.value())
                        .message("Method argument not valid")
                        .detailedMessage(getDetailedMessage(ex))
                        .fieldErrors(fieldErrors)
                        .build(),
                new HttpHeaders(), httpStatus);
    }

    private ErrorResponse buildErrorResponse(String type, HttpStatus httpStatus, Exception ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .error(httpStatus.getReasonPhrase())
                .path(request.getRequestURI())
                .type(type)
                .status(httpStatus.value())
                .message(ex.getMessage())
                .detailedMessage(getDetailedMessage(ex))
                .build();
    }

    private String getDetailedMessage(Exception ex) {
        return ex.getCause() != null ? ex.getCause().getMessage() : null;
    }
}
