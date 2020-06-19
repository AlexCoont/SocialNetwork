package project.handlerExceptions;

public class DataConsistencyException extends RuntimeException {

    public DataConsistencyException(String message) {
        super(message);
    }
}
