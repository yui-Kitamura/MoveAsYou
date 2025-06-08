package eng.pro.yui.mcpl.moveAsYou.mc.data;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

@JsonAdapter(PlayerName.PlayerNameAdapter.class)
public class PlayerName {
    
    private final String name;
    public PlayerName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
    public PlayerName(OfflinePlayer player){
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

    /** recommend to use <code>value</code> method */
    @Override
    public String toString() {
        return name;
    }

    public static class PlayerNameAdapter extends TypeAdapter<PlayerName> {
        @Override
        public void write(JsonWriter out, PlayerName playerName) throws IOException {
            if (playerName == null) {
                out.nullValue();
                return;
            }
            out.value(playerName.value());
        }

        @Override
        public PlayerName read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new PlayerName(in.nextString());
        }
    }
    
}
