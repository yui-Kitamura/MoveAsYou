package eng.pro.yui.mcpl.moveAsYou.auth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum TokenType {
    /** 使い捨て 60分有効 */
    ONE_TIME(false, "onetime"),
    /** 配信者向け 自動延長 */
    STREAMING(true, "stream"),
    /** 管理者向け 無期限 */
    ADMIN(false, "admin");
    
    /** トークン有効期限の既定値（1h） */
    public final static long defaultExpireTime = 60*60*1_000; //ms 
    
    public final boolean isExpireTimeUpdatable;
    public final String alias;
    
    TokenType(boolean isExpireTimeUpdatable, String alias) {
        this.isExpireTimeUpdatable = isExpireTimeUpdatable;
        this.alias = alias;
    }
    
    /** 正式名もしくはaliasからTokenTypeを取得します */
    public static TokenType get(String nameOrAlias){
        if(nameOrAlias == null){ return null; }
        try {
            //nameでの判定
            return TokenType.valueOf(nameOrAlias.toUpperCase());
        }catch(IllegalArgumentException e){
            String lowerInput = nameOrAlias.toLowerCase();
            for(TokenType t : TokenType.values()){
                if(t.alias.equals(lowerInput)){
                    return t;
                }
            }
            return null;
        }
   }

   public static @NotNull List<String> names(){
        List<String> list = new ArrayList<String>();
        list.add(TokenType.ONE_TIME.name());
        list.add(TokenType.STREAMING.name());
        list.add(TokenType.ADMIN.name());
        list.add(TokenType.ONE_TIME.alias);
        list.add(TokenType.STREAMING.alias);
        list.add(TokenType.ADMIN.alias); //小文字なので区別して返す
        return list;
   }
    
}
