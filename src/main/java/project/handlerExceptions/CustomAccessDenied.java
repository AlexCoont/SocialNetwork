package project.handlerExceptions;

import java.nio.file.AccessDeniedException;

public class CustomAccessDenied extends AccessDeniedException {
    public CustomAccessDenied(String file) {
        super(file);
    }

    public CustomAccessDenied(String file, String other, String reason) {
        super(file, other, reason);
    }
}
