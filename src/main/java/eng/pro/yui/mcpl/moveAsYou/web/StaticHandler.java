package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticHandler implements HttpHandler {

    public static final String PATH = "/static/";
    private static final Path BASE_PATH = Paths.get("static/");

    @Override
    public void handle(HttpExchange exchange) {
        String calledPath = exchange.getRequestURI().getPath();
        if (!calledPath.startsWith(PATH) || calledPath.length() == PATH.length()) {
            WebServer.send(400, "resource request is missed", exchange);
            return;
        }
        String resourceName = calledPath.substring(PATH.length());
        try (InputStream resource = MoveAsYou.plugin().getResource("web/static/" + resourceName)){
            if(resource == null) {
                WebServer.send(404, "resource not found", exchange);
                return;
            }
            
            byte[] asBytes = resource.readAllBytes();
            exchange.sendResponseHeaders(200, asBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(asBytes);
                os.flush();
            } //auto-close
        }catch(IOException ioe){
            WebServer.send(ioe, exchange);
        }

    }
}
