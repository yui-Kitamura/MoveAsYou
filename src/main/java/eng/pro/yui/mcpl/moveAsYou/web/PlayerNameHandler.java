package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.config.MoveAsYouConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerNameHandler implements HttpHandler {

    public static final String PATH = "/p/";
    private static final String playerTemplate = "player";

    @Override
    public void handle(HttpExchange exchange) {
        String calledPath = exchange.getRequestURI().getPath();
        if(!calledPath.startsWith(PATH) || calledPath.length() == PATH.length() ) {
            WebServer.send(400, "PlayerName is missed", exchange);
            return;
        }
        
        // 指定されたplayer名
        String pathPlayerName = exchange.getRequestURI().getPath().substring(PATH.length());

        try {
            // online検索
            Player player = MoveAsYou.plugin().getServer().getPlayer(pathPlayerName);
            if (player != null) {
                sendPlayerPage(player, exchange);
                return;
            }

            // offline検索 Deprecated だが代替なし
            OfflinePlayer offlinePlayer = MoveAsYou.plugin().getServer().getOfflinePlayer(pathPlayerName);
            if (offlinePlayer.hasPlayedBefore()) {
                sendPlayerPage(offlinePlayer, exchange);
                //return;
            } else {
                WebServer.send(400, "Player not found", exchange);
                //return;
            }
        }catch(IOException ioe) {
            MoveAsYou.log().throwing(PlayerNameHandler.class.getName(), "handle", ioe);
            ioe.printStackTrace();
        }
    }
    
    private void sendPlayerPage(OfflinePlayer player, HttpExchange exchange) throws IOException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("playerName", player.getName());
        variables.put("playerUuid", player.getUniqueId().toString());
        variables.put("isOnline", player.isOnline());
        variables.put("socketPort", MoveAsYouConfig.socketPort);

        HtmlText fullHtml = HtmlText.getFull(WebServer.getRenderer().render(playerTemplate, variables));
        WebServer.send(200, fullHtml, exchange);
    }
}
