package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
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
        try{
            if(args.length == 0) {
                sendHelp(commandSender);
                return true;
            }
            
            String subCommand = args[0].toLowerCase();
            if(TokenCommand.sub_TOKEN.equals(subCommand)) {
                TokenCommand tokenCommand = new TokenCommand();
                tokenCommand.run(commandSender, args);
                return true;
            }
            if(StatsCommand.sub_STATS.equals(subCommand)) {
                StatsCommand statsCommand = new StatsCommand();
                statsCommand.run(commandSender, args);
                return true;
            }
            if(ConfigCommand.sub_CONFIG.equals(subCommand)) {
                ConfigCommand configCommand = new ConfigCommand();
                configCommand.run(commandSender, args);
                return true;
            }
            if(HelpCommand.sub_HELP.equals(subCommand)) {
                HelpCommand helpCommand = new HelpCommand(label);
                helpCommand.run(commandSender, args);
                return true;
            }
            commandSender.sendMessage(ChatColor.RED + "Unknown sub command: " + subCommand);

        }catch(IllegalArgumentException e) {
            if(e.getMessage().isBlank()) {
                MoveAsYou.log().warning("unknown error while executing command (IllegalArgument)");
                MoveAsYou.log().warning(e.toString());
                commandSender.sendMessage(ChatColor.RED+ "error while executing command (wrong parameter)");
            }else {
                commandSender.sendMessage(ChatColor.RED + "error while executing command (" + e.getMessage() + ")");
            }
            return true;
        }catch(CommandPermissionException e) {
            commandSender.sendMessage(ChatColor.GOLD + e.getMessage());
            return true;
        }

        return true;
    }
    
    public static void sendHelp(CommandSender sender) {
        //TODO implement send help
    }
}
