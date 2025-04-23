package eng.pro.yui.mcpl.moveAsYou.web;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;


public class SimpleWebSocketServer extends WebSocketServer {

    public SimpleWebSocketServer(int port) {
        super(new InetSocketAddress(InetAddress.getLoopbackAddress(), port));
    }
    
    @Override
    public void onStart() {
        MoveAsYou.log().info("Starting WebSocket Server");
    }
    
    @Override
    public void onOpen(WebSocket con, ClientHandshake handshake) {
        MoveAsYou.log().info("WebSocket connected!");
        //TODO implement auth phase
    
    }

    @Override
    public void onClose(WebSocket con, int code, String reason, boolean remote) {
        MoveAsYou.log().info("WebSocket closed: " + reason);
    }

    @Override
    public void onError(WebSocket con, Exception ex) {
        MoveAsYou.log().info("WebSocket error: " + ex);
    }
    
    @Override
    public void onMessage(WebSocket con, String message) {
        con.send("on message");
    }
        
}
