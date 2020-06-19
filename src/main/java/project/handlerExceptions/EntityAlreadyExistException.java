package project.handlerExceptions;

public class EntityAlreadyExistException extends Exception {

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
