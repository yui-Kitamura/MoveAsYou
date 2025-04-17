package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.consts.BgColor;

import java.util.UUID;

public class PlayerSetting {
    
    public UUID playerUUID;
    public String playerName;
    public int BackGroundColor = BgColor.GREEN.getColorCode();
    public boolean DoSneak;
    
}
