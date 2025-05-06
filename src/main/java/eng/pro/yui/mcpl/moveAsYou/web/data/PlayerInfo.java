package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.BgColor;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
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

    private BgColor bgColor;
    public BgColor getBgColor(){ 
        return bgColor;
    }
    
    /** constructor */
    public PlayerInfo(Player player){
        MoveAsYou.log().info("Creating player info for " + player.getName());
        this.playerUuid = player.getUniqueId();
        this.playerName = new PlayerName(player);
        this.bgColor = MoveAsYou.playerSettings().get(player.getUniqueId()).getBackGroundColor();

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
        itemInHand = p.getInventory().getItemInMainHand().getType().name();
    }
    public void notifyAnimation(PlayerAnimationType animation){
        if(animationType == null) {
            animationType = animation;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                animationType = null;
            }
        }.runTaskLater(MoveAsYou.plugin(), 1L); //0.25s
    }
    
    private String worldName;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private boolean isSneaking;
    private String itemInHand;
    private PlayerAnimationType animationType;
    
    public String getWorldName(){ return worldName; }
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getZ(){ return z; }
    public double getYaw(){ return yaw; }
    public double getPitch(){ return pitch; }
    public boolean isSneaking(){ return isSneaking; }
    public String getItemInHand(){ return itemInHand; }
    public String getAnimationType(){ return animationType.name(); }
    

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + playerUuid.hashCode();
        hash = 31 * hash + worldName.hashCode();
        hash = 31 * hash + bgColor.hashCode();
        hash = 31 * hash + Double.valueOf(x).hashCode();
        hash = 31 * hash + Double.valueOf(y).hashCode();
        hash = 31 * hash + Double.valueOf(z).hashCode();
        hash = 31 * hash + Double.valueOf(yaw).hashCode();
        hash = 31 * hash + Double.valueOf(pitch).hashCode();
        hash = 31 * hash + Boolean.valueOf(isSneaking).hashCode();
        hash = 31 * hash + itemInHand.hashCode();
        hash = 31 * hash + animationType.hashCode();
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
        if(bgColor != other.bgColor) { return false; }
        if(x != other.x) { return false; }
        if(y != other.y) { return false; }
        if(z != other.z) { return false; }
        if(yaw != other.yaw) { return false; }
        if(pitch != other.pitch) { return false; }
        if(isSneaking != other.isSneaking) { return false; }
        if(!itemInHand.equals(other.itemInHand)) { return false; }
        if(animationType != other.animationType) { return false; }
        return true;
    }

    @Override
    public String toString() {
        return String.format("PlayerInfo{" +
                        "playerUuid: %s, playerName: %s, bgColor: %s," +
                        " world: %s, x: %.4f, y: %.4f, z: %.4f, yaw: %.4f, pitch: %.4f," +
                        " isSneaking: %b, itemInHand: %s, animationType: %s }",
                playerUuid, playerName, bgColor.name(), worldName, x, y, z, yaw, pitch, 
                isSneaking, itemInHand, animationType.name()
        );
    }
    
    public String toJsonString(){
        return MoveAsYou.gson().toJson(this);
    }
}
