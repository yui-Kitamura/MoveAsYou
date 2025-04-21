package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import org.bukkit.command.CommandSender;

public interface IRateLimiter {

    // methods
    void check(CommandSender playerOrConsole) throws RateLimitedException;
    
}
