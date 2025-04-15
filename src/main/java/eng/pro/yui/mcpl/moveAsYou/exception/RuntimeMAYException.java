package eng.pro.yui.mcpl.moveAsYou.exception;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

public class RuntimeMAYException extends RuntimeException {
    public RuntimeMAYException(String message) {
        super(message);
    }
    public RuntimeMAYException(String message, Throwable cause) {
        super(message, cause);
    }
    public RuntimeMAYException(Throwable cause) {
        super(cause);
    }

    public void log(){
        MoveAsYou.log().warning(this.getMessage());
    }
}
