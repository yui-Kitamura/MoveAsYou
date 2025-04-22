package eng.pro.yui.mcpl.moveAsYou.auth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum TokenType {
    /** 使い捨て 60分有効 */
    ONE_TIME(false),
    /** 配信者向け 自動延長 */
    STREAMING(true),
    /** 管理者向け 無期限 */
    ADMIN(false);
    
    public final static long defaultExpireTime = 60*60*1_000; //1h 
    
    public final boolean isExpireTimeUpdatable;
    
    TokenType(boolean isExpireTimeUpdatable) {
        this.isExpireTimeUpdatable = isExpireTimeUpdatable;
    }
    
    /** 正式名もしくはaliasからTokenTypeを取得します */
    public static TokenType get(String nameOrAlias){
        if(nameOrAlias == null){ return null; }
        try {
            return TokenType.valueOf(nameOrAlias.toUpperCase());
        }catch(IllegalArgumentException e){
            switch(nameOrAlias.toLowerCase()){
                case "onetime":
                    return TokenType.ONE_TIME;
                case "stream":
                    return TokenType.STREAMING;
                default:
                    return null;
            }
        }
   }

   public static @NotNull List<String> names(){
        List<String> list = new ArrayList<String>();
        list.add(TokenType.ONE_TIME.name());
        list.add(TokenType.STREAMING.name());
        list.add(TokenType.ADMIN.name());
        list.add("onetime");
        list.add("stream");
        return list;
   }
    
}
