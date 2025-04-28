package eng.pro.yui.mcpl.moveAsYou.mc.data;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PlayerName {
    
    private final String name;
    public PlayerName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
    public PlayerName(Player player){
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.name = player.getName();
    }
    public PlayerName(ConsoleCommandSender console){
        if(console == null) {
            throw new IllegalArgumentException("Console cannot be null");
        }
        this.name = "*"+console.getName()+"*";
    }
    
    public String value(){
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){ return false; }
        if(obj == this) { return true; }
        if(obj.getClass() != this.getClass()) { return false; }
        PlayerName other = (PlayerName) obj;
        return name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
