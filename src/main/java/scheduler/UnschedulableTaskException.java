package scheduler;

public class UnschedulableTaskException extends RuntimeException {
    public UnschedulableTaskException(String message) {
        super(message);
    }
}
