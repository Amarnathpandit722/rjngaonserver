package gov.municipal.suda.exception.dto;

public class ExceptionHandler extends Exception{
    private String code;

    public ExceptionHandler(String code, String message) {
        super(message);
        this.setCode(code);
    }

    public ExceptionHandler(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
