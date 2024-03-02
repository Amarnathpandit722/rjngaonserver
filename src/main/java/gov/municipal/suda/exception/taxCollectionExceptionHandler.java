package gov.municipal.suda.exception;

import gov.municipal.suda.exception.dto.ErrorDTO;
import gov.municipal.suda.exception.dto.ErrorResponse;
import gov.municipal.suda.message.ResponseMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class taxCollectionExceptionHandler extends ResponseEntityExceptionHandler {

    @lombok.Generated
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException( BadRequestException ex, WebRequest request) {
        logger.info(ex.getLocalizedMessage());

        final List<ErrorDTO> errors= new ArrayList<>();
        ErrorDTO dto = new ErrorDTO(HttpStatus.BAD_REQUEST.toString(), ex.getLocalizedMessage());
        errors.add(dto);
        final ErrorResponse apiError = new ErrorResponse(errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);

    }

    @lombok.Generated
    @ExceptionHandler(AlreadyExistException.class)
    public final ResponseEntity<Object> handleAlreadyExistException( AlreadyExistException ex, WebRequest request) {
        logger.info(ex.getLocalizedMessage());

        final List<ErrorDTO> errors= new ArrayList<>();
        ErrorDTO dto = new ErrorDTO(HttpStatus.CONFLICT.toString(), ex.getLocalizedMessage());
        errors.add(dto);
        final ErrorResponse apiError = new ErrorResponse(errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);

    }

    @lombok.Generated
    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<Object> handleIllegalStateException( IllegalStateException ex, WebRequest request) {
        logger.info(ex.getLocalizedMessage());
        final List<ErrorDTO> errors= new ArrayList<>();
        ErrorDTO dto = new ErrorDTO(HttpStatus.NOT_ACCEPTABLE.toString(), ex.getLocalizedMessage());
        errors.add(dto);
        final ErrorResponse apiError = new ErrorResponse(errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);

    }
    @lombok.Generated
    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException( RecordNotFoundException ex, WebRequest request) {
        logger.info(ex.getLocalizedMessage());

        final List<ErrorDTO> errors= new ArrayList<>();
        ErrorDTO dto = new ErrorDTO(HttpStatus.NOT_FOUND.toString(), ex.getLocalizedMessage());
        errors.add(dto);
        final ErrorResponse apiError = new ErrorResponse(errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);

    }

    @lombok.Generated
    @ExceptionHandler(DatabaseException.class)
    public final ResponseEntity<Object> handleDatabaseException( DatabaseException ex, WebRequest request) {
        logger.info(ex.getLocalizedMessage());

        final List<ErrorDTO> errors= new ArrayList<>();
        ErrorDTO dto = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getLocalizedMessage());
        errors.add(dto);
        final ErrorResponse apiError = new ErrorResponse(errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.OK);

    }
    @lombok.Generated
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File too large!"));
    }
}
