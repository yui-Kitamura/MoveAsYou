package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebServer {
    /** webServer */
    private static HttpServer server;
    //Thymeleaf
    private static ThymeleafRenderer renderer;
    public static ThymeleafRenderer getRenderer(){
        return renderer;
    }
    /** socketServer */
    private static SimpleWebSocketServer socketServer;
    
    public static void create(int webPort, int socketPort){
        createWeb(webPort);
        createSocket(socketPort);
    }
    private static void createWeb(int port){
        if(server != null && server.getAddress().getPort() == port){
            return;
        }
        try {
            stopWeb();
            server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 0); //localhost
        }catch(IOException e) {
            throw new RuntimeMAYException("failed to create web server");
        }

        // pathへのバインド
        server.createContext(MAYHttpHandler.PATH, new MAYHttpHandler()); // contains 404
        server.createContext(PlayerNameHandler.PATH, new PlayerNameHandler());
        server.createContext(AuthHandler.PATH, new AuthHandler());

        renderer = new ThymeleafRenderer();
    } 
    private static void createSocket(int port){
        if(socketServer != null && socketServer.getAddress().getPort() == port){
            return;
        }
        {
            stopSocket();
            socketServer = new SimpleWebSocketServer(port);
        }
    }
    
    
    public static void start(){
        if(server == null){
            MoveAsYou.log().throwing(WebServer.class.getName(), "start", new NullPointerException());
            return;
        }
        try {
            server.start();
            MoveAsYou.log().info("Web server started at " + server.getAddress());
        }catch(Exception unexpected) {
            MoveAsYou.log().severe("FAILED to start web server: " + unexpected.getMessage());
            unexpected.printStackTrace();
            return;
        }
        
        if(socketServer == null){
            MoveAsYou.log().throwing(WebServer.class.getName(), "start", new NullPointerException());
            return;
        }
        try {
            socketServer.start();
        }catch(IllegalStateException unexpected) {
            MoveAsYou.log().severe("FAILED to start socket server: " + unexpected.getMessage());
            unexpected.printStackTrace();
            return;
        }
    }
    public static void stop() {
        stopWeb();
        stopSocket();
    }
    private static void stopWeb() {
        if (server == null) {
            MoveAsYou.log().throwing(WebServer.class.getName(), "stop", new NullPointerException());
            return;
        }
        try {
            MoveAsYou.log().info("Web server stopping at " + server.getAddress() + " --->");
            server.stop(2);
            MoveAsYou.log().info("Web server stopped at " + server.getAddress());
        } catch (Exception unexpected) {
            MoveAsYou.log().severe("FAILED to stop web server: " + unexpected.getMessage());
            unexpected.printStackTrace();
        } finally {
            server = null;
        }
    }
    private static void stopSocket() {
        if(socketServer == null){
            MoveAsYou.log().throwing(WebServer.class.getName(), "stop", new NullPointerException());
            return;
        }
        try {
            MoveAsYou.log().info("Socket server stopping --->");
            socketServer.stop();
            MoveAsYou.log().info("Socket server stopped");
        }catch(Exception unexpected) {
            MoveAsYou.log().severe("FAILED to stop socket server: " + unexpected.getMessage());
            unexpected.printStackTrace();
        }finally {
            socketServer = null;
        }
    }
    
    public static void send(int code, String html, HttpExchange exchange) throws IOException{
        byte[] byteBased = html.getBytes(StandardCharsets.UTF_8);
        
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(code, byteBased.length);
        try(OutputStream out = exchange.getResponseBody()){
            out.write(byteBased);
        }
    }
    
    public static void send(Exception ex, HttpExchange exchange){
        String errMsg = "<!DOCTYPE html><html><body>500 error</body></html>";
        try {
            send(500, errMsg, exchange);
        }catch(IOException ioe) {
            MoveAsYou.log().severe("FAILED to send 500 error: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        MoveAsYou.log().throwing(
                ex.getStackTrace()[0].getClassName(),
                ex.getStackTrace()[0].getMethodName(),
                ex);
        ex.printStackTrace();
    }
    
}
