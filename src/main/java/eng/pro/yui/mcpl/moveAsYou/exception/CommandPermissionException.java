package eng.pro.yui.mcpl.moveAsYou.exception;

public class CommandPermissionException extends RuntimeMAYException {
    
    public CommandPermissionException(){
        this("do not have permission");
    }
    
    public CommandPermissionException(String message) {
        super(message);
    }
}
