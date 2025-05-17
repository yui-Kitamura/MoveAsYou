package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.BgColor;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
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

    private PlayerName playerName;
    public PlayerName getPlayerName() {
        return playerName;
    }
    public void setPlayerName(PlayerName playerName) {
        this.playerName = playerName;
    }

    private BgColor backGroundColor = BgColor.GREEN;
    public BgColor getBackGroundColor() {
        return backGroundColor;
    }
    public void setBackGroundColor(BgColor backGroundColor) {
        this.backGroundColor = backGroundColor;
    }
    
    private boolean doSneak;
    public boolean isDoSneak() {
        return doSneak;
    }
    public void setDoSneak(boolean doSneak) {
        this.doSneak = doSneak;
    }
    
    public PlayerSetting(UUID playerUUID){
        setPlayerUUID(playerUUID);
        setPlayerName(new PlayerName(MoveAsYou.plugin().getServer().getOfflinePlayer(playerUUID).getName()));
        setBackGroundColor(BgColor.GREEN);
        setDoSneak(true);

        MoveAsYou.log().info("Player Setting has created for player " + getPlayerName());
        MoveAsYou.log().info("UUID: " + getPlayerUUID().toString());
        MoveAsYou.log().info("BackGroundColor: " + getBackGroundColor().name());
        MoveAsYou.log().info("DoSneak: " + isDoSneak());
    }
    
    public PlayerSetting(UUID playerUUID, YamlConfiguration config){
        setPlayerUUID(playerUUID);
        setPlayerName(new PlayerName(config.getString("playerName")));
        setBackGroundColor(BgColor.get(config.getString("backGroundColor")));
        setDoSneak(config.getBoolean("doSneak"));

        MoveAsYou.log().info("Player Setting has loaded for player " + getPlayerName());
        MoveAsYou.log().info("UUID: " + getPlayerUUID().toString());
        MoveAsYou.log().info("BackGroundColor: " + getBackGroundColor().name());
        MoveAsYou.log().info("DoSneak: " + isDoSneak());
    }
    
    public YamlConfiguration toFile(){
        YamlConfiguration config = new YamlConfiguration();
        config.set("playerUUID", getPlayerUUID().toString());
        config.set("playerName", getPlayerName().value());
        config.set("backGroundColor", getBackGroundColor().name());
        config.set("doSneak", isDoSneak());
        return config;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + playerUUID.hashCode();
        hash = hash * 31 + playerName.hashCode();
        hash = hash * 31 + backGroundColor.hashCode();
        hash = hash * 31 + Boolean.hashCode(doSneak);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){ return false; }
        if(this == obj){ return true; }
        if(obj.getClass() != this.getClass()) { return false; }
        PlayerSetting other = (PlayerSetting) obj;
        if(!this.playerUUID.equals(other.playerUUID)){ return false; }
        if(!this.playerName.equals(other.playerName)){ return false; }
        if(this.backGroundColor != other.backGroundColor){ return false; }
        if(this.doSneak != other.doSneak){ return false; }
        return true;
    }

    @Override
    public String toString() {
        return String.format("PlayerSetting{UUID:%s,Name:%s,BgColor:%s,Sneak:%s}",
                playerUUID.toString(), playerName.value(), backGroundColor.name(), doSneak
        );
    }
}
