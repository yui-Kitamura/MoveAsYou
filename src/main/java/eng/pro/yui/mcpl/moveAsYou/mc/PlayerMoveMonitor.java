package eng.pro.yui.mcpl.moveAsYou.mc;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import eng.pro.yui.mcpl.moveAsYou.web.data.PlayerInfo;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveMonitor {
    
    private final Map<UUID, PlayerInfo> players; 
    
    public PlayerMoveMonitor() {
        players = new HashMap<>();
    }
    
    public void addPlayer(Player player) {
        if(players.containsKey(player.getUniqueId())) {
            return;
        }
        players.put(player.getUniqueId(), new PlayerInfo(player));
    }
    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }
    public PlayerInfo get(PlayerName name){
        Player online = MoveAsYou.plugin().getServer().getPlayer(name.value());
        if(online == null) { 
            MoveAsYou.log().warning("Player " + name.value() + " not found");
            return null; 
        }
        return players.get(online.getUniqueId());
    }
    
    public Map<PlayerName, PlayerInfo> monitorAll(){
        Map<PlayerName, PlayerInfo> res = new HashMap<>();
        for(PlayerInfo pInfo : players.values()) {
            pInfo.update();
            res.put(pInfo.getName(), pInfo);
        }
        return res;
    }
    public PlayerInfo monitor(Player player){
        if(!players.containsKey(player.getUniqueId())) {
            return null;
        }
        return monitor(player.getUniqueId());
    }
    private PlayerInfo monitor(UUID playerUid){
        Player p = MoveAsYou.plugin().getServer().getPlayer(playerUid);
        if(p == null) {
            return null;
        }
        PlayerInfo info = players.get(playerUid);
        info.update();
        return info;
    }
    
}
