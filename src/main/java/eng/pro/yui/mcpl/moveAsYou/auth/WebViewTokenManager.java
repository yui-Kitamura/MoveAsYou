package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.Consts;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import eng.pro.yui.mcpl.moveAsYou.web.WebServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebViewTokenManager {
    
    public enum ValidateResult {
        OK(true, "ok"),
        NOT_EXISTS(false, "invalid"),
        LIMITED(false, "limited"),
        EXPIRED(false, "expired"),
        USED(false, "used");
        
        public final boolean success;
        public final String message;
        ValidateResult(boolean success, String message){
            this.success = success;
            this.message = message;
        }
        
    }
    
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
    
    public void revokeToken(TokenText token){
        if(tokenStore.containsKey(token) == false) {
            throw new IllegalArgumentException("token not declared");
        }
        tokenStore.get(token).makeDisabled();
        WebServer.socketSendTokenDisabled(token);
        MoveAsYou.log().info("Token("+ token.value() +") revoked");
    }
    
    /** 
     * 有効期限内であることの検証と、期限の延長
     * ONE_TIMEは個人用途に限定 */
    public ValidateResult validate(TokenText token, PlayerName playerName){
        TokenInfo stored = tokenStore.get(token);
        if(stored == null) {
            MoveAsYou.log().warning("Token " + token + ": not exist");
            return ValidateResult.NOT_EXISTS;
        }
        if(stored.isValid() == false){
            MoveAsYou.log().warning("Token " + token + ": already expired");
            return ValidateResult.EXPIRED;
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
                    return ValidateResult.LIMITED;
                }
                break;
        }
       
        stored.refreshTokenActivity();
        return ValidateResult.OK;
    }
    public ValidateResult validateForNew(TokenText token, PlayerName playerName){
        ValidateResult valid = validate(token, playerName);
        if(valid.success) {
            //Tokenの残利用回数も確認する
            TokenInfo stored = tokenStore.get(token);
            if (stored.isAllowedToGenerateNewConnect() == false) {
                MoveAsYou.log().warning("Token " + token.value() + ": player " + playerName + " is not allowed to generate new connect");
                return ValidateResult.USED;
            }
            return ValidateResult.OK;
        }else {
            return valid;
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

    /**
     * @param name 抽出対象プレイヤー名（null可）
     * @return 対象プレイヤーが発行したTokenのリスト。引数がnullの場合、全員
     */
    public List<TokenText> getToken(PlayerName name){
        List<TokenText> result = new ArrayList<>();
        for(TokenInfo info : tokenStore.values()) {
            if (name == null || info.playerName.equals(name)) {
                //null=未指定 or 一致 を返す
                result.add(info.token);
            }
        }
        return result;
    }
    /** typeに合致するTokenのリストを返す */
    public List<TokenText> getToken(TokenType type){
        List<TokenText> result = new ArrayList<>();
        for(TokenInfo info : tokenStore.values()) {
            if (info.tokenType == type) {
                result.add(info.token);
            }
        }
        return result;
    }
    
    /** senderの権限に応じてTokenInfoのテキスト情報リストを返す */
    public List<String> getTokenInfo(CommandSender sender){
        List<TokenInfo> dataSet = new LinkedList<>(tokenStore.values()); 
        List<String> result = new ArrayList<>();
        if(sender instanceof Player p) {
            PlayerName senderName = new PlayerName(p);
            if (sender.hasPermission(Permissions.LIST)) {
                Iterator<TokenInfo> itr = dataSet.iterator();
                while(itr.hasNext()){
                    TokenInfo info = itr.next();
                    if ((info.tokenType != TokenType.ADMIN) && info.playerName.equals(senderName)) {
                        result.add(info.toShortString(TokenText.COLOR));
                        itr.remove();
                    }
                }
            }
            if(sender.hasPermission(Permissions.LIST_OTHERS)) {
                Iterator<TokenInfo> itr = dataSet.iterator();
                while(itr.hasNext()){
                    TokenInfo info = itr.next();
                    if ((info.tokenType != TokenType.ADMIN) && (info.playerName.equals(senderName) == false)) {
                        result.add(info.toShortString(TokenText.COLOR));
                        itr.remove();
                    }
                }
            }
            if(sender.hasPermission(Permissions.LIST_ADMIN)){
                Iterator<TokenInfo> itr = dataSet.iterator();
                while(itr.hasNext()){
                    TokenInfo info = itr.next();
                    if (info.tokenType == TokenType.ADMIN) {
                        result.add(info.toShortString(TokenText.COLOR));
                        itr.remove();
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
                ChatColor cc = (sender instanceof ConsoleCommandSender) ? null : TokenText.COLOR;
                result.add(token.toShortString(cc));
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
        sb.append("Tokens: ").append(tokenStore.size()).append(Consts.br);
        int onetime = 0, stream = 0, admin = 0;
        for(TokenInfo token : tokenStore.values()){
            switch(token.tokenType) {
                case ONE_TIME -> onetime++;
                case STREAMING -> stream++;
                case ADMIN -> admin++;
                default -> {}
            }
        }
        sb.append("Token for ").append(TokenType.ONE_TIME).append(": ").append(onetime).append(Consts.br);
        sb.append("Token for ").append(TokenType.STREAMING).append(": ").append(stream).append(Consts.br);
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
