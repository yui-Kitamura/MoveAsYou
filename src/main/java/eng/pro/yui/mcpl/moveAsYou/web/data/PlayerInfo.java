package eng.pro.yui.mcpl.moveAsYou.web.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class PlayerInfo {
    
    private final UUID playerUuid;
    public UUID getUuid(){
        return playerUuid;
    }
    
    private final PlayerName playerName;
    public PlayerName getName(){
        return playerName;
    }
    
    private final String skinUrl;
    public String getSkinUrl(){
        return skinUrl;
    }
    
    /** constructor */
    public PlayerInfo(Player player){
        this.playerUuid = player.getUniqueId();
        this.playerName = new PlayerName(player);
        String tmpSkinUrl = "";
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            GameProfile profile = (GameProfile) handle.getClass().getMethod("getProfile").invoke(handle);
            for(Property property : profile.getProperties().get("textures")) {
                String value = new String(Base64.getDecoder().decode(property.getValue()), StandardCharsets.UTF_8);
                // JSONに変換
                JsonObject jsonObj = JsonParser.parseString(value).getAsJsonObject();
                if (jsonObj.has("textures")) {
                    JsonObject textures = jsonObj.getAsJsonObject("textures");
                    if (textures.has("SKIN")) {
                        tmpSkinUrl = textures.getAsJsonObject("SKIN").get("url").getAsString();
                    }
                }

            }
        }catch(Exception e) {
            MoveAsYou.log().throwing(PlayerInfo.class.getName(),"constructor", e);
            tmpSkinUrl = "";
        }
        this.skinUrl = tmpSkinUrl;
        update();
    }
    
    public void update(){
        Player p = MoveAsYou.plugin().getServer().getPlayer(playerUuid);
        if(p == null) {
            worldName = "";
            x = 0.0d; y = 0.0d; z = 0.0d;
            yaw = 0.0d; pitch = 0.0d;
            isSneaking = false;
            return;
        }
        worldName = p.getWorld().getName();
        x = p.getLocation().getX();
        y = p.getLocation().getY();
        z = p.getLocation().getZ();
        yaw = p.getLocation().getYaw();
        pitch = p.getLocation().getPitch();
        isSneaking = p.isSneaking();
        boolean isUsingSomething = (p.getItemInUse() != null);
        itemInHand = isUsingSomething ? p.getItemInUse().getData().getItemType().name() : "";
    }
    
    private String worldName;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private boolean isSneaking;
    private String itemInHand;
    
    public String getWorldName(){ return worldName; }
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getZ(){ return z; }
    public double getYaw(){ return yaw; }
    public double getPitch(){ return pitch; }
    public boolean isSneaking(){ return isSneaking; }
    public String getItemInHand(){ return itemInHand; }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + playerUuid.hashCode();
        hash = 31 * hash + worldName.hashCode();
        hash = 31 * hash + skinUrl.hashCode();
        hash = 31 * hash + Double.valueOf(x).hashCode();
        hash = 31 * hash + Double.valueOf(y).hashCode();
        hash = 31 * hash + Double.valueOf(z).hashCode();
        hash = 31 * hash + Double.valueOf(yaw).hashCode();
        hash = 31 * hash + Double.valueOf(pitch).hashCode();
        hash = 31 * hash + Boolean.valueOf(isSneaking).hashCode();
        hash = 31 * hash + itemInHand.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){ return false; }
        if(this == obj) { return true; }
        if(obj.getClass() != this.getClass()) { return false; }
        PlayerInfo other = (PlayerInfo) obj;
        if(!playerUuid.equals(other.playerUuid)) { return false; }
        if(!worldName.equals(other.worldName)) { return false; }
        if(!skinUrl.equals(other.skinUrl)) { return false; }
        if(x != other.x) { return false; }
        if(y != other.y) { return false; }
        if(z != other.z) { return false; }
        if(yaw != other.yaw) { return false; }
        if(pitch != other.pitch) { return false; }
        if(isSneaking != other.isSneaking) { return false; }
        if(!itemInHand.equals(other.itemInHand)) { return false; }
        return true;
    }

    @Override
    public String toString() {
        return String.format("PlayerInfo{" +
                        "playerUuid: %s, playerName: %s, skinUrl: %s," +
                        " world: %s, x: %.4f, y: %.4f, z: %.4f, yaw: %.4f, pitch: %.4f," +
                        " isSneaking: %b, itemInHand: %s}",
                playerUuid, playerName, skinUrl, worldName, x, y, z, yaw, pitch, isSneaking, itemInHand
        );
    }
    
    public String toJsonString(){
        return MoveAsYou.gson().toJson(this);
    }
}
