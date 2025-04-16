package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class MAYHttpHandler implements HttpHandler {
    
    private File rootIndex;
    
    public MAYHttpHandler(){
        this.rootIndex = new File("webroot/index.html");
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestPath = exchange.getRequestURI().getPath();
            String resStr;
            if(requestPath.equals("/")){
                resStr = Files.readString(rootIndex.toPath());
                exchange.sendResponseHeaders(200, resStr.getBytes().length);
            }else{
                resStr = "404 not found";
                exchange.sendResponseHeaders(404, resStr.getBytes().length);
            }
            OutputStream out = exchange.getResponseBody();
            out.write(resStr.getBytes());
            out.close();
        }catch(IOException e){
            throw new RuntimeMAYException(e);
        }
    }
}
