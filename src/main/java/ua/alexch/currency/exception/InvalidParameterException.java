package ua.alexch.currency.exception;

public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static final String DATE_FROM_PARAM = "dateFrom";
    public static final String DATE_TO_PARAM = "dateTo";

    private final String paramName;
    private final String paramValue;

    public InvalidParameterException(String name, String value) {
        super();
        this.paramName = name;
        this.paramValue = value;
    }

    public InvalidParameterException(String name, String value, Throwable cause) {
        super(cause);
        this.paramName = name;
        this.paramValue = value;
    }

    @Override
    public String getMessage() {
        return String.format("Illegal parameter: [%s='%s']", paramName, paramValue);
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamValue() {
        return paramValue;
    }
}
