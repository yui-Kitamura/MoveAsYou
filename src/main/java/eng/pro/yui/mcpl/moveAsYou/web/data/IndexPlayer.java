package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import org.bukkit.OfflinePlayer;
import org.bukkit.profile.PlayerProfile;

public class IndexPlayer {
    
    public final String mcid;
    public final String skinUrl;
    public final String onlineFor;
    
    private static final long hours = 60*60*1000L;
    private static final long minutes = 60*1000L;
    
    public IndexPlayer(OfflinePlayer player){
        this(player, System.currentTimeMillis());
    }
    
    public IndexPlayer(OfflinePlayer player, long baseTimestamp){
        mcid = new PlayerName(player).value();
        PlayerProfile profile = player.getPlayerProfile();
        if(profile.getTextures().getSkin() != null){
            skinUrl = profile.getTextures().getSkin().toString();
        }else {
            skinUrl = "";
        }
        if(player.isOnline()) {
            onlineFor = getLength(baseTimestamp, player.getLastPlayed());
        }else {
            onlineFor = "オフライン";
        }
    }
    
    public static String getLength(long later, long begin){
        long playing = later - begin;
        String hour = (playing >= hours)? (playing/hours + "時間"):"";
        long m = (playing % hours) / minutes;
        String minute = (m > 0 || !hour.isEmpty()) ? String.format("%02d", m) + "分" : "";
        String seconds = String.format("%02d",(playing % minutes) / 1000L) +"秒";
        return hour+minute+seconds;
    }
    
}
