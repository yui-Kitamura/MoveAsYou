package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.auth.TokenInfo;
import eng.pro.yui.mcpl.moveAsYou.auth.TokenType;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.exception.CommandPermissionException;
import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TokenCommand implements ICommand{
    
    public static final String sub_TOKEN = "token";
    
    public TokenCommand(){
        // nothing to do
    }

    public void run(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(args.length < 1 || sub_TOKEN.equals(args[0]) == false) {
            throw new RuntimeMAYException(new IllegalAccessException("wrong command body class has selected"));
        }
        
        String tokenCommandSubOrder = TokenType.ONE_TIME.name();
        if(args.length >= 2) {
            tokenCommandSubOrder = args[1];
        }
        
        switch(tokenCommandSubOrder.toLowerCase()) {
            case "admin": //case "admin":
            case "stream": case "streaming":
            case "onetime": case "one_time":
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
        if(args.length == 2) {
            requestType = TokenType.get(args[1]);
        }
        if(args.length > 2) {
            throw new IllegalArgumentException("too many parameter");
        }
        
        if(requestType == null) {
            throw new IllegalArgumentException("wrong token type");
        }

        if(commandSender instanceof Player sender) {
            if(sender.hasPermission(Permissions.TOKEN) == false) {
                throw new CommandPermissionException();
            }
            TokenInfo generated;
            switch(requestType) {
                case ONE_TIME:
                    generated = MoveAsYou.tokenManager().generateToken(sender, TokenType.ONE_TIME);
                    break;
                case STREAMING:
                    generated = MoveAsYou.tokenManager().generateToken(sender, TokenType.STREAMING);
                    break;
                case ADMIN:
                    if (sender.hasPermission(Permissions.TOKEN_ADMIN) == false) {
                        throw new CommandPermissionException("You don't have permission to issue admin token.");
                    }
                    generated = MoveAsYou.tokenManager().generateToken(sender, TokenType.ADMIN);
                    break;
                default:
                    throw new RuntimeMAYException("wrong token type");
            }

            MoveAsYou.log().info("access token has issued.:" + generated.toString());
            sender.sendMessage(ChatColor.AQUA + generated.token.value());

        }else if(commandSender instanceof ConsoleCommandSender sender) {
            try {
                TokenInfo generated = MoveAsYou.tokenManager().generateToken(sender);
                sender.sendMessage(ChatColor.AQUA + generated.token.value());
            }catch(RateLimitedException rateEx) {
                sender.sendMessage(ChatColor.RED + "Too many request");
            }
            sender.sendMessage(MoveAsYou.tokenManager().getStats());
        }
    }

    /** 
     * <code>/may token list</code><br/>
     * パミの範囲でTokenを表示。
     * 自身のもの、他者のもの、ADMINのもの、の順<br/>
     * <code>/may token list playerName</code><br/>
     * othersパミがあれば表示。自身を指定した場合はothersパミに関わらずlistパミがあれば表示
     */
    private void runList(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if(args.length == 2) {
            /* /may token list
             * 自身の、もしくは権限が許す限り広い範囲のTokenを表示
             *  */
            StringBuilder sb = new StringBuilder();
            for(String s : MoveAsYou.tokenManager().getTokenInfo(commandSender)) {
                sb.append(s).append(MoveAsYou.br);
            }
            commandSender.sendMessage(sb.toString());
            return;
        }
        if(args.length == 3) {
            /* /may token list <playerName>
             * 自身の、もしくはothersのTokenを表示
             * */
            StringBuilder sb = new StringBuilder();
            for (String s : MoveAsYou.tokenManager().getTokensByPlayerName(commandSender, new PlayerName(args[2]))) {
                sb.append(s).append(MoveAsYou.br);
            }
            commandSender.sendMessage(sb.toString());
            return;
        }
        throw new IllegalArgumentException("too many params are given");
    }
    
    private void runRevoke(@NotNull CommandSender commandSender, @NotNull String[] args) {
        throw new RuntimeMAYException(new IllegalAccessException("not implemented")); //FIXME implement
    }

    
}
