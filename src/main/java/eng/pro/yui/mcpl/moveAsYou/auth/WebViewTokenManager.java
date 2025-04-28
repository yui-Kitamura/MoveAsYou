package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
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
    private final Map<TokenText, TokenInfo> tokenStore;
    
    private final ScheduledExecutorService scheduledCleaner;
    
    private final IRateLimiter rateLimiter;

    // constructor
    public WebViewTokenManager() {
        this.tokenStore = new HashMap<>();
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
        TokenInfo generated = new TokenInfo(TokenText.generate(requestType), player, requestType);
        tokenStore.put(generated.token, generated);
        return tokenStore.get(generated.token); 
    }
    public TokenInfo generateToken(ConsoleCommandSender admin) throws RateLimitedException {
        rateLimiter.check(admin);
        TokenInfo generated = new TokenInfo(TokenText.generate(TokenType.ADMIN), admin);
        tokenStore.put(generated.token, generated);
        return tokenStore.get(generated.token);
    }
    
    /** 
     * 有効期限内であることの検証と、期限の延長
     * ONE_TIMEは個人用途に限定 */
    public boolean validate(TokenText token, PlayerName playerName){
        TokenInfo stored = tokenStore.get(token);
        if(stored == null) {
            MoveAsYou.log().warning("Token " + token + ": not exist");
            return false;
        }
        if(stored.isValid() == false){
            MoveAsYou.log().warning("Token " + token + ": already expired");
            return false;
        }
        switch(stored.tokenType) {
            case ADMIN:
                // fall through
            case STREAMING:
                // nothing to do. anybody can access 
                break;
            case ONE_TIME:
                if (stored.playerName.equals(playerName) == false) {
                    MoveAsYou.log().warning("Token " + token + ": player " + playerName + " does not match expected player name");
                    return false;
                }
                break;
        }
       
        stored.refreshTokenActivity();
        return true;
    }
    public boolean validateForNew(TokenText token, PlayerName playerName){
        boolean valid = validate(token, playerName);
        if(valid) {
            //Tokenの残利用回数も確認する
            TokenInfo stored = tokenStore.get(token);
            if (stored.isAllowedToGenerateNewConnect() == false) {
                MoveAsYou.log().warning("Token " + token.value() + ": player " + playerName + " is not allowed to generate new connect");
                return false;
            }
        }
            
    }
    
    public void extendTokenValidity(TokenText token){
        TokenInfo stored = tokenStore.get(token);
        if(stored == null || stored.isValid() == false){
            return;
        }
        stored.refreshTokenActivity();
    }
    
    public boolean forgetToken(TokenText token){
        TokenInfo removed = tokenStore.remove(token);
        if(removed == null) {
            return false;
        }
        if(removed.isValid() == false){
            MoveAsYou.log().info("Removed token " + token.value() + " was already invalid.");
        }
        return true;
    }
    
    public List<TokenText> getTokensByPlayerName(PlayerName playerName){
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
                default -> {}
            }
        }
        sb.append("Token for ").append(TokenType.ONE_TIME).append(": ").append(onetime).append(System.lineSeparator());
        sb.append("Token for ").append(TokenType.STREAMING).append(": ").append(stream).append(System.lineSeparator());
        sb.append("Token for ").append(TokenType.ADMIN).append(": ").append(admin);
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
