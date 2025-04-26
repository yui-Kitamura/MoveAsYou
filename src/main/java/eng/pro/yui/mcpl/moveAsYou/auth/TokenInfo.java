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
    int limitCnt;
    
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
        switch(type){
            case ONE_TIME:
                limitCnt = 1;
                break;
            case STREAMING:
                //fall-through
            case ADMIN:
                limitCnt = Integer.MAX_VALUE;
                break;
            default:    
                limitCnt = 0;
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
                        "generated=%s, lastActivity=%s, expireAt=%s, limitCnt=%s" +
                        "}",
                playerName, tokenType, token,
                formatter.format(Instant.ofEpochMilli(getGeneratedTimeStamp)),
                formatter.format(Instant.ofEpochMilli(lastActivityTimeStamp)),
                formatter.format(Instant.ofEpochMilli(expireAt)), limitCnt
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TokenInfo tokenInfo = (TokenInfo) obj;
        return getGeneratedTimeStamp == tokenInfo.getGeneratedTimeStamp &&
                lastActivityTimeStamp == tokenInfo.lastActivityTimeStamp &&
                expireAt == tokenInfo.expireAt &&
                limitCnt == tokenInfo.limitCnt &&
                playerName.equals(tokenInfo.playerName) &&
                tokenType == tokenInfo.tokenType &&
                token.equals(tokenInfo.token);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + playerName.hashCode();
        result = 31 * result + tokenType.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + Long.hashCode(getGeneratedTimeStamp);
        result = 31 * result + Long.hashCode(lastActivityTimeStamp);
        result = 31 * result + Long.hashCode(expireAt);
        result = 31 * result + limitCnt;
        return result;
    }
}
