package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

import java.util.UUID;

public class AnimationInfo implements ISocketPushInfo {
    
    private UUID playerUuid;
    private PlayerAnimationType animationType;
    
    public AnimationInfo() {
        animationType = null;
    }
    
    public void update(PlayerAnimationEvent event) {
        this.playerUuid = event.getPlayer().getUniqueId();
        this.animationType = event.getAnimationType();
    }

    @Override
    public String getInfoName() {
        return AnimationInfo.class.getSimpleName();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + playerUuid.hashCode();
        hash = hash * 31 + animationType.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){ return false; }
        if(this == obj) { return true; }
        if(obj.getClass() != this.getClass()) { return false; }
        AnimationInfo other = (AnimationInfo) obj;
        if(!playerUuid.equals(other.playerUuid)){ return false;}
        if(animationType != other.animationType) { return false; }
        return true;
    }

    @Override
    public String toString() {
        return String.format("AnimationInfo{" +
                        "animationType: %s }",
                animationType.name()
        );
    }

    public String toJsonString(){
        return MoveAsYou.gson().toJson(this);
    }
}
