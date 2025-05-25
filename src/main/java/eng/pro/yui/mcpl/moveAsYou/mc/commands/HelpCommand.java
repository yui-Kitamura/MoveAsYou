package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.consts.Consts;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pro.eng.yui.yuiframe.YuiFrame;
import pro.eng.yui.yuiframe.consts.IStrConst;

import static eng.pro.yui.mcpl.moveAsYou.consts.Permissions.*;

public class HelpCommand implements ICommand {
    public static final String sub_HELP = "help";
    
    private final String alias;
    
    private enum HelpMode{
        NORMAL,
        CONFIG;
    }
    
    public HelpCommand(String alias){
        this.alias = alias;
    }
    
    @Override
    public void run(CommandSender sender, String[] args) {
        HelpMode mode = HelpMode.NORMAL;
        if(args.length >= 2) {
            /* /may [help,bar] */
            String option = args[1];
            if(YuiFrame.StringUtil.eq(option, HelpMode.CONFIG.name())) {
                mode = HelpMode.CONFIG;
            }
        }
        
        switch (mode) {
            case NORMAL -> sender.sendMessage(runNormalCommandHelp(sender));
            case CONFIG -> sender.sendMessage(runConfigHelp(sender));
            default -> throw new CommandPermissionException("maybe something wrong with help command");
        }
    }
    
    private String runNormalCommandHelp(CommandSender sender){
        StringBuilder sb = new StringBuilder();
        final String calledCmd = ChatColor.AQUA + "/" + alias + IStrConst.SPACE;
        sb.append("===").append(Consts.MAYDecorated).append("===").append(ChatColor.RESET).append(Consts.br);
        
        if(has(sender, Permissions.TOKEN, true)) {
            sb.append(calledCmd).append("token onetime").append(ChatColor.RESET)
                    .append(":").append("1回限り有効の表示連携トークンを発行します").append(Consts.br);
        }
        if(has(sender, Permissions.TOKEN, true)) {
            sb.append(calledCmd).append("token stream").append(ChatColor.RESET)
                    .append(":").append("配信向け再利用可能トークンを発行します").append(Consts.br);
        }
        if(has(sender, Permissions.TOKEN_ADMIN)) {
            sb.append(calledCmd).append("token admin").append(ChatColor.RESET)
                    .append(":").append("管理者トークンを発行します").append(Consts.br);
        }
        if(has(sender, Permissions.LIST)) {
            sb.append(calledCmd).append("token list").append(ChatColor.RESET)
                    .append(":").append("発行したトークンの一覧を表示します");
            if(has(sender, Permissions.LIST_ADMIN)) {
                sb.append("。管理者トークンを含みます");
            }
            sb.append(Consts.br);
        }
        if(has(sender, Permissions.LIST_OTHERS)) {
            sb.append(calledCmd).append("token list ").append(ChatColor.ITALIC).append("playerName").append(ChatColor.RESET)
                    .append(":").append("指定ユーザについて有効なトークンの一覧を表示します");
            sb.append(Consts.br);
        }
        if(has(sender, Permissions.TOKEN)) {
            sb.append(calledCmd).append("token revoke ").append(ChatColor.ITALIC).append("token-code").append(ChatColor.RESET)
                    .append(":").append("指定トークンを無効にします").append(Consts.br);
        }
        if(has(sender, Permissions.TOKEN_ADMIN)) {
            sb.append(calledCmd).append("token revoke all ")
                    .append(ChatColor.ITALIC).append("playerName").append(ChatColor.RESET)
                    .append(":").append("指定プレイヤーのトークンを無効にします").append(Consts.br);
            sb.append(calledCmd).append("token revoke admin").append(ChatColor.RESET)
                    .append(":").append("管理者トークンをすべて無効にします").append(Consts.br);
        }
        if(has(sender, Permissions.STATS_ADMIN)) {
            sb.append(calledCmd).append("stats").append(ChatColor.RESET)
                    .append(":").append("トークンの発行状況を表示します").append(Consts.br);
        }
        
        sb.append(calledCmd).append("help config").append(ChatColor.RESET)
                .append(" でConfigの詳細を表示します");
        
        return sb.toString();
    }
    
    private String runConfigHelp(CommandSender sender){
        /*
         * may config <playerName> <key> <value>
         */
        StringBuilder sb = new StringBuilder();
        final String calledCmd = ChatColor.AQUA + "/" + alias + IStrConst.SPACE;
        sb.append("===").append(Consts.MAYDecorated).append("===")
                .append(ChatColor.DARK_GREEN).append(" = config help =")
                .append(ChatColor.RESET).append(Consts.br);
        
        if(has(sender, CONFIG_SELF_BGCOLOR, true)) {
            sb.append(calledCmd).append("config ").append(sender.getName())
                    .append(" backgroundcolor ")
                    .append(ChatColor.ITALIC).append("colorName").append(ChatColor.RESET)
                    .append(":").append("ウェブ画面で表示する背景色を設定します")
                    .append(ChatColor.RESET).append(Consts.br);
        }
        if(has(sender, CONFIG_ADMIN_BGCOLOR)) {
            sb.append(calledCmd).append("config ").append(ChatColor.ITALIC).append("playerName")
                    .append(ChatColor.RESET).append(ChatColor.AQUA)
                    .append(" bagkgroundcolor ")
                    .append(ChatColor.ITALIC).append("colorName").append(ChatColor.RESET)
                    .append(":").append("指定プレイヤーのウェブ画面で表示する背景色を設定します")
                    .append(ChatColor.RESET).append(Consts.br);
        }
        if(has(sender, CONFIG_SELF_SNEAK, true)) {
            //sb.append()
        }
        if(has(sender, CONFIG_ADMIN_SNEAK)) {
            //sb.append()
        }
        
        return sb.toString();
    }
}
