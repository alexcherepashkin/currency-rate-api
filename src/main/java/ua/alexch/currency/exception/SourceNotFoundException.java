package ua.alexch.currency.exception;

public class SourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static final String SOURCE_PARAM = "source";

    private final String paramValue;

    public SourceNotFoundException(String value) {
        super();
        this.paramValue = value;
    }

    public SourceNotFoundException(String value, Throwable cause) {
        super(cause);
        this.paramValue = value;
    }

    @Override
    public String getMessage() {
        return String.format("Not found by: [%s='%s']", SOURCE_PARAM, paramValue);
    }

    public String getParamName() {
        return SOURCE_PARAM;
    }

    public String getParamValue() {
        return paramValue;
    }
}
