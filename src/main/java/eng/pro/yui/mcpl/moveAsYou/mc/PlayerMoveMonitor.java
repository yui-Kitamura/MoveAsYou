package eng.pro.yui.mcpl.moveAsYou.mc;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.web.data.PlayerInfo;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveMonitor {
    
    private Map<UUID, PlayerInfo> players; 
    
    public PlayerMoveMonitor() {
        players = new HashMap<UUID, PlayerInfo>();
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
    
    public Map<String, PlayerInfo> monitorAll(){
        Map<String, PlayerInfo> res = new HashMap<>();
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
