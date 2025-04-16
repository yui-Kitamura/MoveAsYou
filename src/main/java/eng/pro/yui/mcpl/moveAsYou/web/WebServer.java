package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpServer;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class WebServer extends HttpServer {
    
    public static WebServer create(int port){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            return (WebServer)server;
        }catch(IOException e) {
            throw new RuntimeMAYException("failed to create web server");
        }
    }   
    
}
