package gp.developer.api.task_3.exception;

public class DeveloperUpdateBadRequestException extends Exception{
    private static final String message = "ERROR Developer already has these options";

    public DeveloperUpdateBadRequestException() {
        super(message);
    }
}
