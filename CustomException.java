package com.scb.cos.hk.ms.commons.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private HttpStatus status;
    private List<String> errors;
    
	public CustomException(HttpStatus status, String message, String error, Throwable ex) {
		super(message, ex);
		this.status = status;
		this.errors = Arrays.asList(error);
	}
	
	public CustomException(HttpStatus status, String message, List<String> errors, Throwable ex) {
		super(message, ex);
		this.status = status;
		this.errors = errors;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public List<String> getErrors() {
		return errors;
	}
	
}


@RestControllerAdvice
public class CustomExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@RequestMapping(produces = "application/json")
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException ex) {
		logger.error(ex.getMessage(), ex.getCause());
		ErrorResponse errorResponse = new ErrorResponse(ex);
		return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
	
	@RequestMapping(produces = "application/json")
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
		logger.error(ex.getMessage(), ex.getCause());
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorString = errors.get(fieldName);
			if(errorString == null) {
				errors.put(fieldName, error.getDefaultMessage());
			} else {
				errors.put(fieldName, errorString + error.getDefaultMessage());
			}
		});
		ErrorResponse errorResponse = new ErrorResponse(new CustomException(HttpStatus.BAD_REQUEST, "Validation Failed", new ArrayList<String>(errors.values()), ex));
		return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
	}
	
	@RequestMapping(produces = "application/json")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException ex) {
		logger.error(ex.getMessage(), ex.getCause());
		List<String> errors = new ArrayList<>();
		if(ex.getCause() instanceof InvalidFormatException) {
			InvalidFormatException ie = (InvalidFormatException) ex.getCause();
			errors.add(ie.getValue().toString() + " is rejected for " + ie.getPath().get(0).getFieldName() + ". Expected format: " + ie.getTargetType().getSimpleName());
		} else if(ex.getCause() instanceof MismatchedInputException) {
			MismatchedInputException me = (MismatchedInputException) ex.getCause();
			errors.add("Invalid input for " + me.getPath().get(0).getFieldName() + ". Expected format: " + me.getTargetType().getSimpleName());
		} else if(ex.getCause() instanceof JsonMappingException) {
		    JsonMappingException je = (JsonMappingException) ex.getCause();
            errors.add("Invalid value for " + je.getPath().get(0).getFieldName() + ".");
        } else {
			errors.add("Malformed JSON request.");
		}
		
		ErrorResponse errorResponse = new ErrorResponse(new CustomException(HttpStatus.BAD_REQUEST, "Malformed JSON request", errors, ex));
		return new ResponseEntity<>(errorResponse, errorResponse.getStatus());	
	}
	
	
	@RequestMapping(produces = "application/json")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		logger.error(ex.getMessage(), ex.getCause());
		ErrorResponse errorResponse = new ErrorResponse(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "For Further details, please contact support team.", ex));
		return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}

public class ErrorResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private HttpStatus status;
	private String message;
	private List<String> errors;
		
	public ErrorResponse(CustomException ex) {
		super();
		this.timestamp = LocalDateTime.now();
		this.status = ex.getStatus();
		this.message = ex.getMessage();
		this.errors = ex.getErrors();
	}
	
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public HttpStatus getStatus(){
		return status;
	}

	public String getMessage() {
		return message;
	}


	public List<String> getErrors() {
		return errors;
	}


		
}

