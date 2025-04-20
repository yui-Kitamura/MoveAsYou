package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import org.bukkit.entity.Player;

public interface IRateLimiter {

    // methods
    void check(Player player) throws RateLimitedException;
    
}
