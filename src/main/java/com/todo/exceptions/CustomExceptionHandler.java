package com.todo.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	// ===================== 404 : Handler Not Found
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		ex.printStackTrace();
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		final ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(),
				error);

		return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
	}
	
	// ==================== 500 : Uncaught typed Exceptions
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
		ex.printStackTrace();
		final ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				"Exception Occurred", ex.getLocalizedMessage());
		return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// ===================== 500 : Non Unique Result Exception
	@ExceptionHandler({ CbsExceptionHandler.class })
	public ResponseEntity<Object> handleNonUniqueResultException(final CbsExceptionHandler ex,
			final WebRequest request) {
		ex.printStackTrace();
		final ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				"Duplicate records found", ex.getLocalizedMessage());
		return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
