package eng.pro.yui.mcpl.moveAsYou.auth;

import eng.pro.yui.mcpl.moveAsYou.exception.RateLimitedException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter extends AbstRateLimiter {

    private final RateRecord globalLimit = new RateRecord();
    /** playerName から Rate状況を取得 */
    private final Map<String, RateRecord> playerLimits = new HashMap<>();
    private RateRecord getPlayerRecord(CommandSender s){
        synchronized (playerLimits){
            String name = (s instanceof ConsoleCommandSender) ? String.format("*%s*",s.getName()) : s.getName();
            return playerLimits.computeIfAbsent(name, k -> new RateRecord());
        }
    }
    
    @Override
    public void check(CommandSender player) throws RateLimitedException {
        if(checkGlobalLimit() == false){
            throw new RateLimitedException("global limited situation");
        }
        if(checkPlayerLimit(player) == false) {
            throw new RateLimitedException("player "+player.getName()+" is under limited situation");
        }
    }
    
    private void refresh(Player player) {
        globalLimit.refresh();
        RateRecord record = playerLimits.get(player.getName());
        if(record != null){
            record.refresh();
        }
    }
    
    private boolean checkGlobalLimit(){
        //check global
        synchronized(globalLimit){
            globalLimit.refresh();
            if(globalLimit.minuteRequestCount >= globalLimit.minuteRateLimit) {
                return false;
            }
            if(globalLimit.hourRequestCount >= globalLimit.hourRateLimit) {
                return false;
            }
            if(globalLimit.dayRequestCount >= globalLimit.dayRateLimit) {
                return false;
            }
            globalLimit.incrementCounts();
        }
        return true;
    }
    
    private boolean checkPlayerLimit(CommandSender player){
        RateRecord playerStats = getPlayerRecord(player);
        //check
        synchronized (playerStats) {
            playerStats.refresh();
            if(playerStats.minuteRequestCount >= playerStats.minuteRateLimit){
                return false;
            }
            if(playerStats.hourRequestCount >= playerStats.hourRateLimit){
                return false;
            }
            if(playerStats.dayRequestCount >= playerStats.dayRateLimit){
                return false;
            }
            playerStats.incrementCounts();
        }
        return true;
    }
}
