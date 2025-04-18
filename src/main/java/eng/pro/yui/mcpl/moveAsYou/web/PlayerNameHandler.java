package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PlayerNameHandler implements HttpHandler {

    public static final String PATH = "/p/";
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String pathPlayerName = exchange.getRequestURI().getPath().substring(PATH.length());
        
    }
}
