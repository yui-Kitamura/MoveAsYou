package eng.pro.yui.mcpl.moveAsYou.mc;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.config.PluginVersion;
import eng.pro.yui.mcpl.moveAsYou.consts.Consts;
import eng.pro.yui.mcpl.moveAsYou.consts.Permissions;
import eng.pro.yui.mcpl.moveAsYou.web.WebServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventHandlers implements Listener {

    @EventHandler
    public void onLoginEvent(PlayerLoginEvent event){
        MoveAsYou.playerSettings().loadPlayerSetting(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        if(Permissions.has(event.getPlayer(), Permissions.STATS_ADMIN)) {
            PluginVersion.read();
            if(PluginVersion.hasLatest()) {
                event.getPlayer().sendMessage(Consts.MAYDecorated +" has released new version. Now:"+ PluginVersion.CURRENT +" -> Latest:"+ PluginVersion.latest);
            }else {
                event.getPlayer().sendMessage(Consts.MAYDecorated+ " is running with the latest version ("+ PluginVersion.CURRENT +")");
            }
        }
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
