package eng.pro.yui.mcpl.moveAsYou.web.data;

import org.java_websocket.WebSocket;
import java.util.Arrays;

public class SocketID {
    
    private byte[] id;

    // constructor
    public SocketID(byte[] id) {
        this.id = id;
    }
    public SocketID(WebSocket socket){
        this.id = socket.getRemoteSocketAddress().getAddress().getAddress();
    }
    
    // methods
    public byte[] getIdArray(){
        return id;
    }
    public String getIdValue(){
        return new String(id);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){ return false; }
        if(obj.getClass().equals(this.getClass())){
            return this.getIdValue().equals(((SocketID)obj).getIdValue());            
        }
        return false;
    }

    @Override
    public String toString() {
        return getIdValue();
    }
}
