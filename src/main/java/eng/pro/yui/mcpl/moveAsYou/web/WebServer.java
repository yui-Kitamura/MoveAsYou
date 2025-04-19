package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
    
    private static HttpServer server;
    //Thymeleaf
    private static ThymeleafRenderer renderer;
    public static ThymeleafRenderer getRenderer(){
        return renderer;
    }
    
    public static void create(int port){
        if(server != null && server.getAddress().getPort() == port){
            return;
        }
        try {
            stop();
            server = HttpServer.create(new InetSocketAddress(port), 0);
        }catch(IOException e) {
            throw new RuntimeMAYException("failed to create web server");
        }
        
        // pathへのバインド
        server.createContext(MAYHttpHandler.PATH, new MAYHttpHandler()); // contains 404
        server.createContext(PlayerNameHandler.PATH, new PlayerNameHandler());
        
        renderer = new ThymeleafRenderer();
    }
    public static void start(){
        if(server == null){
            MoveAsYou.log().throwing(WebServer.class.getName(), "start", new NullPointerException());
            return;
        }
        server.start();
    }
    public static void stop(){
        if(server == null){
            MoveAsYou.log().throwing(WebServer.class.getName(), "stop", new NullPointerException());
            return;
        }
        server.stop(0);
    }
    
    public static void send(int code, String html, HttpExchange exchange) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(code, html.length());
        try(OutputStream out = exchange.getResponseBody()){
            out.write(html.getBytes());
        }
    }
    
}
