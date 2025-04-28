package eng.pro.yui.mcpl.moveAsYou.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eng.pro.yui.mcpl.moveAsYou.web.data.HtmlText;

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
            HtmlText fullHtml = HtmlText.getFull(WebServer.getRenderer().render(rootIndex, null));
            WebServer.send(200, fullHtml, exchange);
        }else{
            resStr = "404 not found";
            WebServer.send(404, resStr, exchange);
        }
    }
}
