package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.auth.WebViewTokenManager;
import eng.pro.yui.mcpl.moveAsYou.web.data.AuthRequestInfo;

import java.io.IOException;
import java.io.InputStream;

public class AuthHandler implements HttpHandler {

    public static final String PATH = "/auth/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String calledPath = exchange.getRequestURI().getPath();
        if (!PATH.equals(calledPath)) {
            throw new IllegalArgumentException("Not a supported path: " + calledPath);
        }

        if (! "POST".equals(exchange.getRequestMethod())) {
            WebServer.sendPlane(405, "Method not allowed", exchange);
            return;
        }

        AuthRequestInfo requestInfo;
        try(InputStream in = exchange.getRequestBody()) {
            requestInfo = MoveAsYou.gson().fromJson(
                    new String(in.readAllBytes()), AuthRequestInfo.class
            );
        }catch(IllegalArgumentException argEx) {
            MoveAsYou.log().info("Invalid request: " + argEx.getMessage());
            requestInfo = null;
        }
        MoveAsYou.log().info("Auth request: " + requestInfo);

        if (requestInfo == null || requestInfo.token == null || requestInfo.playerName == null) {
            WebServer.sendPlane(400, "Invalid request body", exchange);
            return;
        }

        WebViewTokenManager.ValidateResult valid = MoveAsYou.tokenManager().validate(requestInfo.token, requestInfo.playerName);
        if(valid.success) {
            WebServer.sendPlane(200, "OK", exchange);
        }else {
            WebServer.sendPlane(400, valid.message, exchange);
        }

    }

}
