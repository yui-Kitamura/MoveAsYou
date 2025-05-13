package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.auth.TokenType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import pro.eng.yui.yuiframe.YuiFrame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MAYTabCompleter implements TabCompleter {

    public MAYTabCompleter() {
        // nothing to do
    }

    private final List<String> first = Arrays.asList("token","stats");
    private final List<String> tokenList = Arrays.asList(
            TokenType.ONE_TIME.alias, TokenType.STREAMING.alias, TokenType.ADMIN.alias,
            "list", "revoke"
    );
    private final List<String> tokenRevokeList = Arrays.asList("all", "admin");
    private final List<String> playerName = null;

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                      @NotNull String alias, String[] args) {
        /* 
         * may token onetime
         * may token stream
         * may token admin
         * may token list
         * may token list <playerName>
         * may token revoke
         * may token revoke all <playerName>
         * may token revoke admin
         * may stats
         */
        if(args.length == 0) {
            return filter(first, "");
        }
        if(args.length == 1) {
            return filter(first, args[0]);
        }
        if(args.length == 2) {
            /* /may token ? */
            if(YuiFrame.StringUtil.eq(args[0], "token")) {
                return filter(tokenList, args[1]);
            }
            /* /may stats ? */
            if(YuiFrame.StringUtil.eq(args[0], "stats")) {
                return Collections.emptyList();
            }
        }
        if(args.length == 3) {
            /* /may token xxx ? */
            if(YuiFrame.StringUtil.eq(args[0], "token")) {
                if(YuiFrame.StringUtil.eq(args[1], "list")) {
                    return playerName;
                }
                if(YuiFrame.StringUtil.eq(args[1], "revoke")) {
                    return filter(tokenRevokeList, args[2]);
                }
            }
        }
        if(args.length == 4) {
            /* /may token revoke all ? */
            if(YuiFrame.StringUtil.eq(args[0], "token")) {
                if (YuiFrame.StringUtil.eq(args[1], "revoke")) {
                    if (YuiFrame.StringUtil.eq(args[2], "all")) {
                        return playerName;
                    }
                }
            }
        }
        return Collections.emptyList();
    }
    
    /* pkg-prv */ List<String> filter(List<String> list, String input){
        return list.stream().filter(s -> s.toLowerCase().startsWith(input.toLowerCase())).toList();
    }
}
