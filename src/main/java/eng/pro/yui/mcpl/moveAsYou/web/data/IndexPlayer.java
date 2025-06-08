package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

public class IndexPlayer {
    
    public final String mcid;
    public final String skinUrl;
    
    public IndexPlayer(Player player){
        mcid = new PlayerName(player).value();
        PlayerProfile profile = player.getPlayerProfile();
        if(profile.getTextures().getSkin() != null){
            skinUrl = profile.getTextures().getSkin().toString();
        }else {
            skinUrl = "";
        }
    }
    
}
