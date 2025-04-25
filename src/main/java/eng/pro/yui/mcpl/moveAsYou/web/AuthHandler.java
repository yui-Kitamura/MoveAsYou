package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.web.data.AuthRequestInfo;

import java.io.IOException;
import java.io.InputStreamReader;

public class AuthHandler implements HttpHandler {

    public static final String PATH = "/auth/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String calledPath = exchange.getRequestURI().getPath();
        if (!PATH.equals(calledPath)) {
            throw new IllegalArgumentException("Not a supported path: " + calledPath);
        }

        if (! "POST".equals(exchange.getRequestMethod())) {
            WebServer.send(405, "Method not allowed", exchange);
            return;
        }

        AuthRequestInfo requestInfo = MoveAsYou.gson().fromJson(
                new InputStreamReader(exchange.getRequestBody()),
                AuthRequestInfo.class
        );

        if (requestInfo == null || requestInfo.token == null || requestInfo.playerName == null) {
            WebServer.send(400, "Invalid request body", exchange);
            return;
        }
        
        //TODO implement
        
        
    }

}
