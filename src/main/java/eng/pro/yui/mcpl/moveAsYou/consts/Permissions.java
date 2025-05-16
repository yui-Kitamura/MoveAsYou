package eng.pro.yui.mcpl.moveAsYou.consts;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

import org.bukkit.permissions.Permission;
import java.util.List;

public final class Permissions {
    private Permissions(){ /* インスタンス化禁止 */ }

    /** トークンを発行する基本権限 */
    public static Permission TOKEN;
    /** 管理者向けトークンを有効にする拡張権限 */
    public static Permission TOKEN_ADMIN;
    /** 自身のトークン一覧を表示する権限 */
    public static Permission LIST;
    /** 他人のトークン一覧を表示する権限 */
    public static Permission LIST_OTHERS;
    /** 管理者向けトークンを含めて一覧表示する権限 */
    public static Permission LIST_ADMIN;
    /** 管理者向け トークンの全体状況把握 */
    public static Permission STATS_ADMIN;
    /** 自身の背景色設定 */
    public static Permission CONFIG_SELF_BGCOLOR;
    /** 他プレイヤー分の背景色設定 */
    public static Permission CONFIG_ADMIN_BGCOLOR;
    
    static {
        List<Permission> list = MoveAsYou.plugin().getDescription().getPermissions();
        for(Permission p : list) {
            switch(p.getName()) {
                case "moveAsYou.token":
                    TOKEN = p; break;
                case "moveAsYou.token.admin":
                    TOKEN_ADMIN = p; break;
                case "moveAsYou.list":
                    LIST = p; break;
                case "moveAsYou.list.others":
                    LIST_OTHERS = p; break;
                case "moveAsYou.list.admin":
                    LIST_ADMIN = p; break;
                case "moveAsYou.stats.admin":
                    STATS_ADMIN = p; break;
                case "moveAsYou.config.self.bgcl":
                    CONFIG_SELF_BGCOLOR = p; break;
                case "moveAsYou.config.admin.bgcl":
                    CONFIG_ADMIN_BGCOLOR = p; break;
                default:
                    throw new IllegalArgumentException("unexpected permission declared:" + p.getName());
            }
        }
    }

}
