package ua.alexch.currency.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ua.alexch.currency.util.DateTimeUtil;

@ApiModel(value = "ErrorResponse")
public class ApiError {

    @ApiModelProperty(value = "${prop.err-type.description}", example = "Unexpected Error")
    private ErrorType error;

    @ApiModelProperty(example = "02-02-2020 02:00:20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime timestamp;

    @ApiModelProperty(value = "${prop.err-msg.description}")
    private String message;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(ErrorType error, String message) {
        this();
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error.getError();
    }

    public void setError(ErrorType error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
