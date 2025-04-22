package eng.pro.yui.mcpl.moveAsYou.mc;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.auth.TokenInfo;
import eng.pro.yui.mcpl.moveAsYou.auth.TokenType;
import eng.pro.yui.mcpl.moveAsYou.auth.WebViewTokenManager;
import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TokenCommand implements ICommand{
    
    public static final String sub_TOKEN = "token";
    private static final WebViewTokenManager manager = new WebViewTokenManager();
    public static String getStats(){
        return manager.getStats();
    }
    
    public TokenCommand(){
        // nothing to do
    }

    public void run(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(args.length < 1 || sub_TOKEN.equals(args[0]) == false) {
            throw new RuntimeMAYException(new IllegalAccessException("wrong command body class has selected"));
        }
        
        String tokenCommandSubOrder = "issue";
        if(args.length >= 2) {
            tokenCommandSubOrder = args[1];
        }
        
        switch(tokenCommandSubOrder) {
            case "issue":
            case "admin":
            case "stream":
            case "onetime":
                runIssue(commandSender, args);
                break;
            case "list":
                runList(commandSender, args);
                break;
            case "revoke":
                runRevoke(commandSender, args);
                break;
            // cleanup, revoke-all 検討
            default:
                MAYCommandHandler.sendHelp(commandSender);
        }
    }
    
    private void runIssue(@NotNull CommandSender commandSender, @NotNull String[] args){
        TokenType requestType = null;
        if(args.length == 1) {
            requestType = TokenType.ONE_TIME;
        }
        if(args.length >= 2) {
            requestType = TokenType.get(args[1]);
        }
        if(requestType == null) {
            commandSender.sendMessage(ChatColor.RED + "wrong token type");
            return;
        }

        if(commandSender instanceof Player sender) {
            if(sender.hasPermission("moveAsYou.token") == false) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return;
            }
            TokenInfo generated;
            switch(requestType) {
                case ONE_TIME:
                    generated = manager.generateToken(sender, TokenType.ONE_TIME);
                    break;
                case STREAMING:
                    generated = manager.generateToken(sender, TokenType.STREAMING);
                    break;
                case ADMIN:
                    if (sender.hasPermission("moveAsYou.token.admin") == false) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to issue admin token.");
                        return;
                    }
                    generated = manager.generateToken(sender, TokenType.ADMIN);
                    break;
                default:
                    throw new RuntimeMAYException("wrong token type");
            }

            MoveAsYou.log().info("access token has issued.:" + generated.toString());
            sender.sendMessage(ChatColor.AQUA + generated.token.value());

        }else if(commandSender instanceof ConsoleCommandSender sender) {
            try {
                TokenInfo generated = manager.generateToken(sender);
                sender.sendMessage(ChatColor.AQUA + generated.token.value());
            }catch(RateLimitedException rateEx) {
                sender.sendMessage(ChatColor.RED + "Too many request");
            }
            sender.sendMessage(manager.getStats());
        }
    }
    
    private void runList(@NotNull CommandSender commandSender, @NotNull String[] args) {
        throw new RuntimeMAYException(new IllegalAccessException("not implemented")); //FIXME implement
    }
    
    private void runRevoke(@NotNull CommandSender commandSender, @NotNull String[] args) {
        throw new RuntimeMAYException(new IllegalAccessException("not implemented")); //FIXME implement
    }

    
}
