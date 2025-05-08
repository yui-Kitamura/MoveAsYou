package eng.pro.yui.mcpl.moveAsYou.mc;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.web.WebServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventHandlers implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        MoveAsYou.playerSettings().loadPlayerSetting(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        MoveAsYou.playerSettings().unloadPlayerSetting(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        WebServer.socketSendEvent(event);
    }

}
