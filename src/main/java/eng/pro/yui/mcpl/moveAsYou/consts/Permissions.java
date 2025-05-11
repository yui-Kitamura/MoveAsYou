package eng.pro.yui.mcpl.moveAsYou.consts;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

import org.bukkit.permissions.Permission;
import java.util.List;

public final class Permissions {
    private Permissions(){ /* インスタンス化禁止 */ }
    
    static {
        List<Permission> list = MoveAsYou.plugin().getDescription().getPermissions();
        for(Permission p : list) {
            switch(p.getName()) {
                case "moveAsYou.token":
                    TOKEN = p; break;
                case "moveAsYou.token.admin":
                    TOKEN_ADMIN = p; break;
                case "moveAsYou.stats.admin":
                    STATS_ADMIN = p; break;
                default:
                    throw new IllegalArgumentException("unexpected permission is declared:" + p.getName());
            }
        }
    }
    public static Permission TOKEN;
    public static Permission TOKEN_ADMIN;
    public static Permission STATS_ADMIN;
    
}
