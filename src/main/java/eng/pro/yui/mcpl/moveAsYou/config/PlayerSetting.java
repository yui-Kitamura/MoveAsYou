package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.consts.BgColor;

import java.util.UUID;

public class PlayerSetting {
    
    private UUID playerUUID;
    public UUID getPlayerUUID() {
        return playerUUID;
    }
    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    private String playerName;
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private BgColor backGroundColor = BgColor.GREEN;
    public BgColor getBackGroundColor() {
        return backGroundColor;
    }
    public void setBackGroundColor(BgColor backGroundColor) {
        this.backGroundColor = backGroundColor;
    }
    
    private boolean DoSneak;
    public boolean isDoSneak() {
        return DoSneak;
    }
    public void setDoSneak(boolean doSneak) {
        DoSneak = doSneak;
    }

}
