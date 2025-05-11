package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
import org.bukkit.command.CommandSender;

public class StatsCommand implements ICommand {
    public static final String sub_STATS = "stats";
    
    public StatsCommand(){
        // nothing to do
    }
    
    @Override
    public void run(CommandSender sender, String[] args){
        if(sender.hasPermission(Permissions.STATS_ADMIN) == false) {
            throw new CommandPermissionException();
        }
        sender.sendMessage(MoveAsYou.tokenManager().getStats());
    }
}
