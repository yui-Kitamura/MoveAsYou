package eng.pro.yui.mcpl.moveAsYou.consts;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permissible;
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
    /** 自身のスニーク演出設定 */
    public static Permission CONFIG_SELF_SNEAK;
    /** 他プレイヤー分のスニーク演出設定 */
    public static Permission CONFIG_ADMIN_SNEAK;
    
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
                case "moveAsYou.config.self.sneak":
                    CONFIG_SELF_SNEAK = p; break;
                case "moveAsYou.config.admin.sneak":
                    CONFIG_ADMIN_SNEAK = p; break;
                default:
                    throw new IllegalArgumentException("unexpected permission declared:" + p.getName());
            }
        }
    }
    /** 
     * @return 権限を有する時<code>true</code>、それ以外は<code>false</code>. ただし、Consoleからは常に<code>true</code> */
    public static boolean has(Permissible entity, Permission permission){
        return has(entity, permission, false);
    }

    /**
     * @param ignoreConsole Consoleからの実行を拒否する場合<code>true</code>
     * @return 権限の有無をbooleanで返す. ただし、Consoleについてはignoreフラグを考慮する.
     */
    public static boolean has(Permissible entity, Permission permission, boolean ignoreConsole){
        if(entity instanceof ConsoleCommandSender) { return !ignoreConsole; }

        return entity.hasPermission(permission);
    }

}
