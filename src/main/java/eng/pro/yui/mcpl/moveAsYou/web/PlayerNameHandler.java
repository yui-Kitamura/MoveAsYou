package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerNameHandler implements HttpHandler {

    public static final String PATH = "/p/";
    private static final String playerTemplate = "player";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String calledPath = exchange.getRequestURI().getPath();
        if(!calledPath.startsWith(PATH) || calledPath.length() == PATH.length() ) {
            WebServer.send(400, "PlayerName is missed", exchange);
            return;
        }
        
        String pathPlayerName = exchange.getRequestURI().getPath().substring(PATH.length());

        Player player = MoveAsYou.plugin().getServer().getPlayer(pathPlayerName);
        
        try {
            if (player == null) {
                WebServer.send(400, "Player not found", exchange);
                return;
            }

            Map<String, Object> variables = new HashMap<>();
            variables.put("playerName", pathPlayerName);
            variables.put("playerUid", player.getUniqueId().toString());

            String html = WebServer.getRenderer().render(playerTemplate, variables);
            WebServer.send(200, html, exchange);

        }catch(IOException ioe) {
            WebServer.send(ioe, exchange);
        }
    }
}
