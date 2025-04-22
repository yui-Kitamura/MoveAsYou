package eng.pro.yui.mcpl.moveAsYou.mc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MAYCommandHandler implements CommandExecutor {
    
    public static final String COMMAND = "MoveAsYou";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            sendHelp(commandSender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        if(TokenCommand.sub_TOKEN.equals(subCommand)) {
            TokenCommand tokenCommand = new TokenCommand();
            return tokenCommand.onCommand(commandSender, args);
        }
        // /ma stats
        
        commandSender.sendMessage(ChatColor.RED + "Unknown sub command: " + subCommand);
        return true;
    }
    
    public static void sendHelp(CommandSender sender) {
        //TODO implement send help
    }
}
