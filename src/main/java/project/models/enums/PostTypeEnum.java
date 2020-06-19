package project.models.enums;

public enum PostTypeEnum {
    POSTED("POSTED"),
    QUEUED("QUEUED");

    private final String type;

    PostTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
