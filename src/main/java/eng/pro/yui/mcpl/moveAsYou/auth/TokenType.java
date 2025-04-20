package eng.pro.yui.mcpl.moveAsYou.auth;

public enum TokenType {
    /** 使い捨て 60分有効 */
    ONE_TIME,
    /** 配信者向け 自動延長 */
    STREAMING,
    /** 管理者向け 無期限 */
    ADMIN;
    
    public final static long defaultExpireTime = 60*60*1_000; //1h 
}
