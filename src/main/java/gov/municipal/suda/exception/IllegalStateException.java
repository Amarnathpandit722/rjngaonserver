package gov.municipal.suda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class IllegalStateException extends RuntimeException{
    public IllegalStateException(String exception) {
        super(exception);
    }
}
