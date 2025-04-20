package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TokenInfo {
    final String playerName;
    final TokenType tokenType;
    final TokenText token;
    final long getGeneratedTimeStamp;
    long lastActivityTimeStamp;
    long expireAt;
    
    public TokenInfo(TokenText token, Player player, TokenType type){
        this.playerName = player.getName();
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
