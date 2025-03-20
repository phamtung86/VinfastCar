package com.vinfast.config.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {
	@Autowired
	private MessageSource messageSource;

	private String getMessage(String key) {
		return messageSource.getMessage(key, null, Locale.getDefault());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
		StringBuilder message = new StringBuilder(exception.getMethod() + " " + getMessage("HttpRequestMethodNotSupportedException.message"));

		if (exception.getSupportedHttpMethods() != null) {
			message.append(" Supported Methods: ");
			for (HttpMethod method : exception.getSupportedHttpMethods()) {
				message.append(method).append(" ");
			}
		}

		return buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, message.toString());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception) {
		StringBuilder message = new StringBuilder();
		message.append(exception.getContentType()).append(" ").append(getMessage("HttpMediaTypeNotSupportedException.message"));

		if (!exception.getSupportedMediaTypes().isEmpty()) {
			message.append(" Supported: ");
			for (MediaType mediaType : exception.getSupportedMediaTypes()) {
				message.append(mediaType).append(", ");
			}
			message.setLength(message.length() - 2); // Xóa dấu ", " cuối cùng
		}

		return buildResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message.toString());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();

		for (ObjectError error : exception.getBindingResult().getAllErrors()) {
			if (error instanceof FieldError fieldError) {
				errors.put(fieldError.getField(), fieldError.getDefaultMessage());
			} else {
				errors.put(error.getObjectName(), error.getDefaultMessage());
			}
		}

		return buildResponseEntity(HttpStatus.BAD_REQUEST, getMessage("MethodArgumentNotValidException.message"), errors);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
		String requiredType = (exception.getRequiredType() != null) ? exception.getRequiredType().getName() : "Unknown Type";
		String message = exception.getName() + " " + getMessage("MethodArgumentTypeMismatchException.message") + requiredType;
		return buildResponseEntity(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAll(Exception exception) {
		String detailMessage = exception.getClass().getName() + ": " + exception.getLocalizedMessage();
		return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("Exception.message"), detailMessage);
	}

	private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("timestamp", LocalDateTime.now());
		return new ResponseEntity<>(body, status);
	}

	private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, Object details) {
		Map<String, Object> body = new HashMap<>();
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("details", details);
		body.put("timestamp", LocalDateTime.now());
		return new ResponseEntity<>(body, status);
	}
}
