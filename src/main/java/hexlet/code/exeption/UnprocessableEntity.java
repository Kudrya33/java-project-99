package hexlet.code.exeption;

public class UnprocessableEntity extends RuntimeException {
    public UnprocessableEntity(String message) {
        super(message);
    }
}
