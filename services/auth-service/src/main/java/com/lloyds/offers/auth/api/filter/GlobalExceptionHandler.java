package com.lloyds.offers.auth.api.filter;

import com.lloyds.offers.auth.application.command.DuplicateAccountException;
import com.lloyds.offers.auth.application.command.InvalidOtpException;
import com.lloyds.offers.auth.application.command.OtpLockedException;
import com.lloyds.offers.auth.application.command.RateLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateAccountException.class)
    public ProblemDetail handleDuplicate(DuplicateAccountException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setType(URI.create("https://api.offers.lloyds.com/errors/duplicate-account"));
        problem.setTitle("Account already exists");
        return problem;
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ProblemDetail handleInvalidOtp(InvalidOtpException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setType(URI.create("https://api.offers.lloyds.com/errors/invalid-otp"));
        problem.setTitle("Invalid OTP");
        problem.setProperty("attemptsRemaining", ex.getAttemptsRemaining());
        return problem;
    }

    @ExceptionHandler(OtpLockedException.class)
    public ProblemDetail handleLocked(OtpLockedException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, "Account locked for 30 minutes");
        problem.setType(URI.create("https://api.offers.lloyds.com/errors/otp-locked"));
        problem.setTitle("Too many attempts");
        return problem;
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ProblemDetail handleRateLimit(RateLimitExceededException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded");
        problem.setType(URI.create("https://api.offers.lloyds.com/errors/rate-limited"));
        problem.setTitle("Too many requests");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setType(URI.create("https://api.offers.lloyds.com/errors/validation"));
        problem.setTitle("Invalid request");
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage()).toList();
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problem.setType(URI.create("https://api.offers.lloyds.com/errors/internal"));
        problem.setTitle("Internal server error");
        // Never expose stack trace or internal details
        return problem;
    }
}
