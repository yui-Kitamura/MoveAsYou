package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebViewTokenManager {
    
    /** tokenからtokenInfoを取得 */
    private final Map<String, TokenInfo> tokenStore;
    
    private final ScheduledExecutorService scheduledCleaner;
    
    private final IRateLimiter rateLimiter;

    // constructor
    public WebViewTokenManager() {
        this.tokenStore = new HashMap<String, TokenInfo>();
        this.scheduledCleaner = Executors.newSingleThreadScheduledExecutor();
        this.scheduledCleaner.scheduleAtFixedRate(
                this::cleanupExpiredTokens,
                60, 60, TimeUnit.SECONDS
        );
        rateLimiter = new RateLimiter();
    }
    public void shutdown(){
        this.scheduledCleaner.shutdown();
    }
    
    // methods
    
    public TokenInfo generateToken(Player player, TokenType requestType) throws RateLimitedException {
        rateLimiter.check(player); // case NG: throws Exception
        TokenInfo generated = new TokenInfo(TokenText.generate(), player, requestType);
        tokenStore.put(generated.token.value(), generated);
        return tokenStore.get(generated.token.value()); 
    }
    public TokenInfo generateToken(ConsoleCommandSender admin) throws RateLimitedException {
        rateLimiter.check(admin);
        TokenInfo generated = new TokenInfo(TokenText.generate(), admin);
        tokenStore.put(generated.token.value(), generated);
        return tokenStore.get(generated.token.value());
    }
    
    public boolean validate(TokenText token){
        TokenInfo stored = tokenStore.get(token.value());
        if(stored == null || stored.isValid() == false){
            return false;
        }
        stored.refreshTokenActivity();
        return true;
    }
    
    public void extendTokenValidity(TokenText token){
        TokenInfo stored = tokenStore.get(token.value());
        if(stored == null || stored.isValid() == false){
            return;
        }
        stored.refreshTokenActivity();
    }
    
    public boolean forgetToken(TokenText token){
        TokenInfo removed = tokenStore.remove(token.value());
        if(removed == null) {
            return false;
        }
        if(removed.isValid() == false){
            MoveAsYou.log().info("Removed token " + token.value() + " was already invalid.");
        }
        return true;
    }
    
    public List<TokenText> getTokensByPlayerName(String playerName){
         List<TokenText> tokens = new ArrayList<TokenText>();
         for(TokenInfo token : tokenStore.values()){
             if(token.playerName.equals(playerName)){
                 tokens.add(token.token);
             }
         }
         MoveAsYou.log().info("Found tokens count for player " + playerName + " is "+ tokens.size());
         return tokens;
    }
    
    public String getStats(){
        StringBuilder sb = new StringBuilder();
        sb.append("Tokens: ").append(tokenStore.size()).append(System.lineSeparator());
        int onetime = 0, stream = 0, admin = 0;
        for(TokenInfo token : tokenStore.values()){
            switch(token.tokenType) {
                case ONE_TIME -> onetime++;
                case STREAMING -> stream++;
                case ADMIN -> admin++;
                default -> {
                }
            }
        }
        sb.append("Token for ").append(TokenType.ONE_TIME).append(": ").append(onetime).append(System.lineSeparator());
        sb.append("Token for ").append(TokenType.STREAMING).append(": ").append(stream).append(System.lineSeparator());
        sb.append("Token for ").append(TokenType.ADMIN).append(": ").append(admin).append(System.lineSeparator());
        return sb.toString();
    }
    
    private void cleanupExpiredTokens(){
        for(TokenInfo token : tokenStore.values()){
            if(token.isValid() == false){
                forgetToken(token.token);
            }
        }
    }
    
}
