package gg.al.exception;

/**
 * Created by Thomas Neumann on 31.03.2017.
 */
public class EntityException extends RuntimeException {
    public EntityException() {
    }

    public EntityException(String s) {
        super(s);
    }

    public EntityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EntityException(Throwable throwable) {
        super(throwable);
    }

    public EntityException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
