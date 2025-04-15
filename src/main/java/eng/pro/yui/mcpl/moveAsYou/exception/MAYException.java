package eng.pro.yui.mcpl.moveAsYou.exception;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

public class MAYException extends Exception {
    
    public MAYException(String message){
        super(message);
    }
    
    public void log(){
        MoveAsYou.log().warning(this.getMessage());
    }
}
