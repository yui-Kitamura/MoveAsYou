package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StatsCommand implements ICommand {
    public static final String sub_STATS = "stats";
    
    public StatsCommand(){
        // nothing to do
    }
    
    @Override
    public void run(CommandSender sender, String[] args){
        if(sender.hasPermission(Permissions.STATS_ADMIN) == false) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
        }
        sender.sendMessage(MoveAsYou.tokenManager().getStats());
    }
}
