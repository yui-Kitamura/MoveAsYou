package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MAYTabCompleter implements TabCompleter {

    public MAYTabCompleter() {
        // nothing to do
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                      @NotNull String alias, String[] args) {
        List<String> resultList = new ArrayList<>();
        
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
        
        //TODO implement

        return resultList;
    }
    
    /* pkg-prv */ List<String> filter(List<String> list, String input){
        return list.stream().filter(s -> s.toLowerCase().startsWith(input.toLowerCase())).toList();
    }
}
