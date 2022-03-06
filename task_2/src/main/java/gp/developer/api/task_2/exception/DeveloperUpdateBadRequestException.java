package gp.developer.api.task_2.exception;

public class DeveloperUpdateBadRequestException extends Exception{
    private static final String message = "ERROR Developer already has these options";

    public DeveloperUpdateBadRequestException() {
        super(message);
    }
}
