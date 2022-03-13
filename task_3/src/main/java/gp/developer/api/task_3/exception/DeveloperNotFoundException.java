package gp.developer.api.task_3.exception;

public class DeveloperNotFoundException extends Exception{
    private static final String message = "ERROR The developer with this requested parameters was not found";

    public DeveloperNotFoundException() {
        super(message);
    }
}
