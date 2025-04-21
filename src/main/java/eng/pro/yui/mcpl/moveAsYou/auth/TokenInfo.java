package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TokenInfo {
    /* pkg-prv */ final String playerName;
    /* pkg-prv */ final TokenType tokenType;
    public final TokenText token;
    /* pkg-prv */ final long getGeneratedTimeStamp;
    /* pkg-prv */ long lastActivityTimeStamp;
    /* pkg-prv */ long expireAt;
    
    public TokenInfo(TokenText token, Player player, TokenType type) {
        this(token, player.getName(), type);
    }
    public TokenInfo(TokenText token, ConsoleCommandSender console) {
        this(token, String.format("*%s*",console.getName()), TokenType.ADMIN);
    }
    private TokenInfo(TokenText token, String authName, TokenType type){
        this.playerName = authName;
        this.tokenType = type;
        this.token = token;
        this.getGeneratedTimeStamp = System.currentTimeMillis();
        this.lastActivityTimeStamp = System.currentTimeMillis();
        switch(type){
            case ONE_TIME: 
                //fall-through
            case STREAMING:
                expireAt = lastActivityTimeStamp + TokenType.defaultExpireTime;
                break;
            case ADMIN:
                expireAt = Long.MAX_VALUE;
                break;
            default:
                expireAt = lastActivityTimeStamp;
                break;
        }
    }
    /** 
     * 最終アクセス時間の更新
     * ※許される場合は有効期限も更新する */
    public void refreshTokenActivity(){
        lastActivityTimeStamp = System.currentTimeMillis();
        if(tokenType.isExpireTimeUpdatable){
            expireAt = System.currentTimeMillis() + TokenType.defaultExpireTime;
        }
    }
    /** 期限内か検証 */
    public boolean isValid(){
        return expireAt > System.currentTimeMillis();
    }


    public String toJsonStr(){
        return MoveAsYou.compactGson().toJson(this);
    }
    public static TokenInfo fromJson(String json){
        return MoveAsYou.compactGson().fromJson(json, TokenInfo.class);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                .withZone(ZoneId.systemDefault());

        return String.format("TokenInfo{" +
                        "playerName='%s', tokenType=%s, token=%s, " +
                        "generated=%s, lastActivity=%s, expireAt=%s" +
                        "}",
                playerName, tokenType, token,
                formatter.format(Instant.ofEpochMilli(getGeneratedTimeStamp)),
                formatter.format(Instant.ofEpochMilli(lastActivityTimeStamp)),
                formatter.format(Instant.ofEpochMilli(expireAt))
        );
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
