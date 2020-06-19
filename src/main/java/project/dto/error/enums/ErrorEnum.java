package project.dto.error.enums;

public enum ErrorEnum {
    INVALID_REQUEST("invalid_request"),
    UNAUTHORIZED("unauthorized");

    private final String error;

    ErrorEnum(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
