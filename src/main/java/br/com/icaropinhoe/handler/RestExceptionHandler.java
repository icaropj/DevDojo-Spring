package br.com.icaropinhoe.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.icaropinhoe.error.ErrorDetails;
import br.com.icaropinhoe.error.ResourceNotFoundDetails;
import br.com.icaropinhoe.error.ResourceNotFoundException;
import br.com.icaropinhoe.error.ValidationErrorDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex){
		ResourceNotFoundDetails resourceNotFoundDetails = ResourceNotFoundDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())
			.title("Resource not found")
			.detail(ex.getMessage())
			.developerMessage(ex.getClass().getName())
			.build();
		return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.NOT_FOUND);
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
//		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
//		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
//		
//		ValidationErrorDetails resourceNotFoundDetails = ValidationErrorDetails.Builder
//			.newBuilder()
//			.timestamp(new Date().getTime())
//			.status(HttpStatus.NOT_FOUND.value())
//			.title("Validation Error")
//			.detail("Validation Error")
////			.developerMessage(ex.getClass().getName())
//			.field(fields)
//			.fieldMessage(fieldMessages)
//			.build();
//		return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.NOT_FOUND);
//	}
	//Apagado dps de extends ResponseEntityExceptionHandler
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
		
		ValidationErrorDetails resourceNotFoundDetails = ValidationErrorDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())
			.title("Validation Error")
			.detail("Validation Error")
//			.developerMessage(ex.getClass().getName())
			.field(fields)
			.fieldMessage(fieldMessages)
			.build();
		return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.NOT_FOUND);
	}
	
//	@Override
//	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		ErrorDetails resourceNotFoundDetails = ErrorDetails.Builder
//				.newBuilder()
//				.timestamp(new Date().getTime())
//				.status(HttpStatus.BAD_REQUEST.value())
//				.title("Message Not Readable")
//				.detail(ex.getMessage())
//				.developerMessage(ex.getClass().getName())
//				.build();
//		return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.BAD_REQUEST);
//	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ErrorDetails resourceNotFoundDetails = ErrorDetails.Builder
				.newBuilder()
				.timestamp(new Date().getTime())
				.status(status.value())
				.title("Internal Exception")
				.detail(ex.getMessage())
				.developerMessage(ex.getClass().getName())
				.build();
		return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.BAD_REQUEST);
	}
	
}
