package eng.pro.yui.mcpl.moveAsYou.web;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

import eng.pro.yui.mcpl.moveAsYou.auth.TokenText;
import eng.pro.yui.mcpl.moveAsYou.auth.WebViewTokenManager;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import eng.pro.yui.mcpl.moveAsYou.web.data.AnimationInfo;
import eng.pro.yui.mcpl.moveAsYou.web.data.ConfigUpdateInfo;
import eng.pro.yui.mcpl.moveAsYou.web.data.PlayerInfo;
import eng.pro.yui.mcpl.moveAsYou.web.data.SocketID;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


public class SimpleWebSocketServer extends WebSocketServer {
    
    private static final String playerListenerPath = "/socket/player/";
    
    private final Map<SocketID, NetworkInfo> connectionAndPlayer;
    private static class NetworkInfo {
        public TokenText activatedToken = null;
        public void setToken(TokenText token){
            if(activatedToken == null) {
                this.activatedToken = token;
            }
        }
        public final long startTimestamp;
        public final PlayerName playerName;
        public NetworkInfo(PlayerName name){
            startTimestamp = System.currentTimeMillis();
            playerName = name;
        }
    }

    public SimpleWebSocketServer(int port) {
        super(new InetSocketAddress(InetAddress.getLoopbackAddress(), port));
        connectionAndPlayer = new HashMap<>();
    }
    
    @Override
    public void onStart() {
        MoveAsYou.log().info("Starting WebSocket Server");
    }
    
    @Override
    public void onOpen(WebSocket con, ClientHandshake handshake) {
        String endPoint = handshake.getResourceDescriptor();
        if(endPoint.startsWith(playerListenerPath) == false){
            con.close(4004, "endpoint missed");
            MoveAsYou.log().info("wrong Socket request:" + endPoint);
            return;
        }
        if(endPoint.length() == playerListenerPath.length()){
            con.close(4001, "parameter missed");
            return;
        }
        PlayerName playerName = new PlayerName(endPoint.substring(playerListenerPath.length()));
        connectionAndPlayer.put(new SocketID(con), new NetworkInfo(playerName));

        MoveAsYou.log().info("WebSocket connected! ("+ playerName + ")");
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
        MoveAsYou.log().info("Received message: " + message);
        if(message.startsWith("Token:")) {
            onMessageToken(con, message);
            return;
        }
        con.send("server doing nothing for your message");
    }
    
    private void onMessageToken(WebSocket con, String message){
        String token = message.substring("Token:".length());
        TokenText t = new TokenText(token);
        SocketID socketID = new SocketID(con);
        PlayerName pn = connectionAndPlayer.get(socketID).playerName;
        WebViewTokenManager.ValidateResult result = MoveAsYou.tokenManager().validateForNew(t, pn);
        if(result.success){
            Player online = MoveAsYou.plugin().getServer().getPlayer(pn.value());
            if(online == null){ 
                con.close(4001, "player not found");
                return; 
            }
            MoveAsYou.log().info("Received token: " + t + " is good for player " + pn.value());
            MoveAsYou.playerMonitor().addPlayer(online);
            connectionAndPlayer.get(socketID).setToken(t);
        }else {
            switch(result) {
                case NOT_EXISTS -> con.close(4000, result.message);
                case EXPIRED -> con.close(4100, result.message);
                case USED -> con.close(4200, result.message);
                case LIMITED -> con.close(4300, result.message);
                default -> con.close(4000, "bad request");
            }
            return;
        }
    }
    
    public void sendPlayerInfo(){
        for(WebSocket con : getConnections()){
            SocketID key = new SocketID(con);
            NetworkInfo info = connectionAndPlayer.get(key);
            PlayerInfo pi = MoveAsYou.playerMonitor().get(info.playerName);
            if(System.currentTimeMillis()-info.startTimestamp > 1_000L) {
                if (pi == null) {
                    con.close(4001, "player disconnected");
                    return;
                }
                if (con.isOpen()) {
                    con.send(pi.toJsonString());
                }
            }else{ /* 1s待機 */ }
        }
    }
    
    public void sendPlayerAnimationEvent(PlayerAnimationEvent e){
        AnimationInfo sendInfo = new AnimationInfo();
        sendInfo.update(e);
        PlayerName target = new PlayerName(e.getPlayer());
        for(WebSocket con : getConnections()){
            SocketID key = new SocketID(con);
            if(target.equals(connectionAndPlayer.get(key).playerName)) {
                con.send(sendInfo.toJsonString());
            }
        }        
    }
    
    public void sendPlayerConfigChanged(PlayerName targetPlayer){
        ConfigUpdateInfo info = new ConfigUpdateInfo();
        for(WebSocket con : getConnections()) {
            SocketID key = new SocketID(con);
            if (targetPlayer.equals(connectionAndPlayer.get(key).playerName)) {
                con.send(info.toJsonString());
            }
        }
    }
    
    public void sendTokenRevoked(TokenText token){
        for(WebSocket con : getConnections()) {
            SocketID key = new SocketID(con);
            if (connectionAndPlayer.get(key).activatedToken.equals(token)) {
                con.close(4003, "token now disabled");
            }
        }
    }
        
}
