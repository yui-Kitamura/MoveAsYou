package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;
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
            return true;
        }else {
            return false;
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
    
    /** senderの権限に応じてTokenInfoのテキスト情報リストを返す */
    public List<String> getTokenInfo(CommandSender sender){
        List<TokenInfo> dataSet = new ArrayList<>(tokenStore.values()); 
        List<String> result = new ArrayList<>();
        if(sender instanceof Player p) {
            PlayerName senderName = new PlayerName(p);
            if (sender.hasPermission(Permissions.LIST)) {
                for (TokenInfo info : dataSet) {
                    if ((info.tokenType != TokenType.ADMIN) && info.playerName.equals(senderName)) {
                        result.add(info.toShortString());
                        dataSet.remove(info);
                    }
                }
            }
            if(sender.hasPermission(Permissions.LIST_OTHERS)) {
                for (TokenInfo info : dataSet) {
                    if ((info.tokenType != TokenType.ADMIN) && (info.playerName.equals(senderName) == false)) {
                        result.add(info.toShortString());
                        dataSet.remove(info);
                    }
                }
            }
            if(sender.hasPermission(Permissions.LIST_ADMIN)){
                for(TokenInfo info : dataSet) {
                    if (info.tokenType == TokenType.ADMIN) {
                        result.add(info.toShortString());
                        dataSet.remove(info);
                    }
                }
            }
        }else if(sender instanceof ConsoleCommandSender) {
            for (TokenInfo info : dataSet) {
                result.add(info.toShortString());
            }
        }
        if(result.isEmpty()) {
            result.add("nothing to show");
        }
        return result;
    }
    /** senderの権限とrequestのplayerNameに応じてTokenInfoのテキスト情報リストを返す */
    public List<String> getTokensByPlayerName(CommandSender sender, PlayerName playerName){
        if(sender instanceof ConsoleCommandSender && playerName == null) {
            throw new IllegalArgumentException("required parameter PlayerName is missed");
        }
        if(sender instanceof Player p) {
            if (playerName == null) {
                playerName = new PlayerName(p);
            }
            if (new PlayerName(p).equals(playerName)) {
                if (p.hasPermission(Permissions.LIST) == false) {
                    throw new CommandPermissionException();
                }
            } else {
                if (p.hasPermission(Permissions.LIST_OTHERS) == false) {
                    throw new CommandPermissionException();
                }
            }
        }
        //permission OK
        List<String> result = new ArrayList<>();
        for(TokenInfo token : tokenStore.values()){
            if(token.playerName.equals(playerName)){
                result.add(token.toShortString());
            }
        }
        MoveAsYou.log().info("Found tokens count for player " + playerName + " is "+ result.size());
        return result;
    }

    /**
     * 現在保存されているトークンの統計情報を取得します。<br/>
     * 以下の情報を含む文字列を返します：
     * <ul>
     * <li>全トークン数</li>
     * <li>ONE_TIMEトークンの数</li>
     * <li>STREAMINGトークンの数</li>
     * <li>ADMINトークンの数</li>
     * </ul>
     * @return 各統計情報が改行で区切られた文字列
     */
    public String getStats(){
        StringBuilder sb = new StringBuilder();
        sb.append("Tokens: ").append(tokenStore.size()).append(MoveAsYou.br);
        int onetime = 0, stream = 0, admin = 0;
        for(TokenInfo token : tokenStore.values()){
            switch(token.tokenType) {
                case ONE_TIME -> onetime++;
                case STREAMING -> stream++;
                case ADMIN -> admin++;
                default -> {}
            }
        }
        sb.append("Token for ").append(TokenType.ONE_TIME).append(": ").append(onetime).append(MoveAsYou.br);
        sb.append("Token for ").append(TokenType.STREAMING).append(": ").append(stream).append(MoveAsYou.br);
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
