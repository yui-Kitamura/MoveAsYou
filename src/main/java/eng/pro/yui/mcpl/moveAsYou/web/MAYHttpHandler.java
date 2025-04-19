package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MAYHttpHandler implements HttpHandler {
    
    public static final String PATH = "/";
    private final String rootIndex = "index";
    
    public MAYHttpHandler(){
        //nothing to do
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String resStr;
            if(PATH.equals(requestPath)){
                String html = WebServer.getRenderer().render(rootIndex, null);
                WebServer.send(200, html, exchange);
            }else{
                resStr = "404 not found";
                WebServer.send(404, resStr, exchange);
            }
        }catch(IOException e){
            WebServer.send(e, exchange);
        }
    }
}
