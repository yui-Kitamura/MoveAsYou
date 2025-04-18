package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.BgColor;
import org.bukkit.configuration.file.YamlConfiguration;

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
    
    public PlayerSetting(UUID playerUUID){
        setPlayerUUID(playerUUID);
        setPlayerName(MoveAsYou.plugin().getServer().getOfflinePlayer(playerUUID).getName());
        setBackGroundColor(BgColor.GREEN);
        setDoSneak(true);

        MoveAsYou.log().info("Player Setting has created for player " + getPlayerName());
        MoveAsYou.log().info("UUID: " + getPlayerUUID());
        MoveAsYou.log().info("BackGroundColor: " + getBackGroundColor().name());
        MoveAsYou.log().info("DoSneak: " + isDoSneak());
    }
    
    public PlayerSetting(UUID playerUUID, YamlConfiguration config){
        setPlayerUUID(playerUUID);
        setPlayerName(config.getString("playerName"));
        setBackGroundColor(BgColor.get(config.getInt("backGroundColor")));
        setDoSneak(config.getBoolean("doSneak"));

        MoveAsYou.log().info("Player Setting has loaded for player " + getPlayerName());
        MoveAsYou.log().info("UUID: " + getPlayerUUID());
        MoveAsYou.log().info("BackGroundColor: " + getBackGroundColor().name());
        MoveAsYou.log().info("DoSneak: " + isDoSneak());
    }
    
    public YamlConfiguration toFile(){
        YamlConfiguration config = new YamlConfiguration();
        config.set("playerUUID", getPlayerUUID());
        config.set("playerName", getPlayerName());
        config.set("backGroundColor", getBackGroundColor().name());
        config.set("doSneak", isDoSneak());
        return config;
    }

}
