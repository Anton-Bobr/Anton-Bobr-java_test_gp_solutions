package gp.developer.api.task_3.exception;

public class DeveloperMailNotUniqueException extends Exception {
    private static final String message = "ERROR Developer with this email exists";

    public DeveloperMailNotUniqueException() {
        super(message);
    }
}
