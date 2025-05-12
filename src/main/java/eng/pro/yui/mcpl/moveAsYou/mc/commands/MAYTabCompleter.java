package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MAYTabCompleter implements TabCompleter {

    public MAYTabCompleter() {
        // nothing to do
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                      @NotNull String alias, String[] args) {
        List<String> resultList = new ArrayList<>();
        
        //TODO implement

        return resultList;
    }
}
