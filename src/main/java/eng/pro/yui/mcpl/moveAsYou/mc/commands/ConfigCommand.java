package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.config.PlayerSetting;
import eng.pro.yui.mcpl.moveAsYou.consts.BgColor;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pro.eng.yui.yuiframe.YuiFrame;

public class ConfigCommand implements ICommand {
    public static final String sub_CONFIG = "config";
    
    public ConfigCommand(){
        // nothing to do
    }
    
    @Override
    public void run(CommandSender sender, String[] args){
        if(args.length < 1 || YuiFrame.StringUtil.eq(sub_CONFIG, args[0]) == false) {
            throw new RuntimeMAYException(new IllegalAccessException("wrong command body class has selected"));
        }
        if(args.length < 5) {
            throw new IllegalArgumentException("required parameters are missed");
        }

        final PlayerName targetName = new PlayerName(args[1]);
        final String configKey = args[2].toLowerCase();
        final String newValue = args[3];

        switch(configKey) {
            case "backgroundcolor":
                changeBgColor(sender, targetName, BgColor.get(newValue));
                break;
            case "dosneak":
                changeDoSneak(sender, targetName, Boolean.parseBoolean(newValue));
                break;
            default:
                throw new IllegalArgumentException("unknown config parameter name");
        }
            
    }
    
    private PlayerSetting getCurrentSetting(PlayerName name){
        OfflinePlayer player = MoveAsYou.plugin().getServer().getPlayer(name.value());
        if(player == null) {
            player = MoveAsYou.plugin().getServer().getOfflinePlayer(name.value());
        }
        if(player.hasPlayedBefore() == false) {
            throw new IllegalArgumentException("unknown player name");
        }
        return MoveAsYou.playerSettings().get(player.getUniqueId());
    }
    
    private void changeBgColor(CommandSender sender, PlayerName targetName, BgColor newValue){
        PlayerSetting current = getCurrentSetting(targetName);
        
        if(sender instanceof ConsoleCommandSender) {
            current.setBackGroundColor(newValue);
            MoveAsYou.playerSettings().updatePlayerSetting(current.getPlayerUUID(), current);
        }
        
        if(sender instanceof Player inGame){
            if(new PlayerName(inGame).equals(targetName)) {
                if (sender.hasPermission(Permissions.CONFIG_SELF_BGCOLOR) == false) {
                    throw new CommandPermissionException();
                }
            }else {
                if (sender.hasPermission(Permissions.CONFIG_ADMIN_BGCOLOR) == false) {
                    throw new CommandPermissionException("you do not have permission to change other players config");
                }
            }
            
            current.setBackGroundColor(newValue);
            MoveAsYou.playerSettings().updatePlayerSetting(current.getPlayerUUID(), current);
        }
    }
    
    private void changeDoSneak(CommandSender sender, PlayerName targetName, boolean newValue){
        PlayerSetting current = getCurrentSetting(targetName);

        if(sender instanceof ConsoleCommandSender) {
            current.setDoSneak(newValue);
            MoveAsYou.playerSettings().updatePlayerSetting(current.getPlayerUUID(), current);
        }

        if(sender instanceof Player inGame){
            if(new PlayerName(inGame).equals(targetName)) {
                if (sender.hasPermission(Permissions.CONFIG_SELF_SNEAK) == false) {
                    throw new CommandPermissionException();
                }
            }else {
                if (sender.hasPermission(Permissions.CONFIG_ADMIN_SNEAK) == false) {
                    throw new CommandPermissionException("you do not have permission to change other players config");
                }
            }

            current.setDoSneak(newValue);
            MoveAsYou.playerSettings().updatePlayerSetting(current.getPlayerUUID(), current);
        }
    }
}
