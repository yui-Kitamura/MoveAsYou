package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.web.data.HtmlText;
import eng.pro.yui.mcpl.moveAsYou.web.data.IndexPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class MAYHttpHandler implements HttpHandler {
    
    public static final String PATH = "/";
    private final String rootIndex = "index";
    
    public MAYHttpHandler(){
        //nothing to do
    }
    
    @Override
    public void handle(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String resStr;
        if(PATH.equals(requestPath)){
            HtmlText fullHtml = renderIndex(exchange);
            WebServer.send(200, fullHtml, exchange);
        }else{
            resStr = "404 not found";
            WebServer.sendPlane(404, resStr, exchange);
        }
    }
    
    private HtmlText renderIndex(HttpExchange exchange){
        Map<String, Object> variables = new HashMap<>();
        variables.put("users", getPlayerList());
        return HtmlText.getFull(WebServer.getRenderer().render(rootIndex, variables));
    }
    
    private List<IndexPlayer> getPlayerList(){
        Collection<? extends Player> all = MoveAsYou.plugin().getServer().getOnlinePlayers();
        
        List<IndexPlayer> result = new ArrayList<>();
        for(Player p : all) {
            result.add(new IndexPlayer(p));
        }
        Collections.shuffle(result);
        while(result.size() > 10) {
            result.remove(result.size()-1);
        }
        
        return result;
    }
    
}
