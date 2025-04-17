package eng.pro.yui.mcpl.moveAsYou.mc;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventHandlers implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        MoveAsYou.log().info(event.getPlayer().getName() + " joined");
        ;
    }

}
