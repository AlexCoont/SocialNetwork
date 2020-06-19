package project.dto.error.enums;

public enum ErrorDescriptionEnum {
    UNAUTHORIZED("Unauthorized"),
    CODE_SUPPLIED("An authorization code must be supplied"),
    REDIRECT_MISMATCH("Redirect URI mismatch"),
    INVALID_AUTH_CODE("Invalid authorization code: CODE"),
    BAD_CREDENTIALS("Bad credentials"),
    BAD_REQUEST("Bad request");

    private final String error;

    ErrorDescriptionEnum(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }



}
