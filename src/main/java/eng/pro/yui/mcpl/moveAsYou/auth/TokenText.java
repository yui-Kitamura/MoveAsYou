package eng.pro.yui.mcpl.moveAsYou.auth;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonAdapter(TokenText.TokenTextAdapter.class)
public final class TokenText {
    
    public static final ChatColor COLOR = ChatColor.AQUA;
    private static final String DELIMITER = "-";
    private static final SecureRandom random = new SecureRandom();
    
    private final String value;
    public String value(){
        return value;
    }
    public String[] getParts(){
        return value.split(DELIMITER);
    }
    
    public TokenText(String value){
        if(isValid(value)){
            this.value = value;
        } else {
            throw new IllegalArgumentException("Invalid token text");
        }
    }
    
    @Contract("_ -> new")
    public static @NotNull TokenText generate(@NotNull TokenType type){
        switch(type){
            case ONE_TIME -> { return generateToken(3); }
            case STREAMING -> { return new TokenText(generateToken(3).value + "-stream"); }
            case ADMIN -> { return generateToken(4); }
            default -> throw new IllegalArgumentException("Invalid token type");
        }
    }
    private static TokenText generateToken(int wordCount){
        if(wordCount < 2 || 6 < wordCount){
            throw new IllegalArgumentException("Invalid token count (must be between 3 and 5)");
        }
        List<String> parts = new ArrayList<>();
        parts.add(MOBS.get(random.nextInt(MOBS.size())));
        parts.add(RARE_BLOCKS.get(random.nextInt(RARE_BLOCKS.size())));
        
        for(int i = 2; i < wordCount; i++){
            parts.add(all().get(random.nextInt(all().size())));
        }
        Collections.shuffle(parts);
        if(wordCount > 3) {
            int random4dig = 1000 + random.nextInt(9000); //1000～9999
            parts.add(String.valueOf(random4dig));
        }

        return new TokenText(String.join(DELIMITER, parts));
    }
    
    public static boolean isValid(String applicant){
        if(applicant == null || applicant.isBlank()){
            return false;
        }

        String[] parts = applicant.split(DELIMITER);
        if (parts.length < 3 || 5 < parts.length) {
            return false;
        }
        for (String part : parts) {
            if (part.length() < 3 || 9 < part.length()) {
                return false;
            }
        }
        return true;
    }

    // Block（ブロック系）
    private static final List<String> BLOCKS = List.of(
            "acacia", "bamboo", "birch", "brick",
            "cactus", "cherry", "clay", "cobble", "glass",
            "honey", "jungle", "nether", "obsidian", "paper", "prism",
            "quartz", "redstone", "spruce", "stone", "wool"
    );

    // Mob（モブ/生物系）
    private static final List<String> MOBS = List.of(
            "blaze", "ender", "leather", "phantom",
            "sheep", "slime", "turtle", "wither"
    );

    // Ore, Rare Blocks（鉱石・レアブロック）
    private static final List<String> RARE_BLOCKS = List.of(
            "amethyst", "beacon", "coal", "copper", "diamond",
            "elytra", "emerald", "gold", "iron",
            "quartz", "totem", "trident"
    );
    private static List<String> all(){
        List<String> all = new ArrayList<>(BLOCKS);
        all.addAll(MOBS);
        all.addAll(RARE_BLOCKS);
        return all;
    }
    

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        TokenText other = (TokenText) obj;
        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static class TokenTextAdapter extends TypeAdapter<TokenText> {
        @Override
        public void write(JsonWriter out, TokenText tokenText) throws IOException {
            if (tokenText == null) {
                out.nullValue();
                return;
            }
            out.value(tokenText.value());
        }

        @Override
        public TokenText read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new TokenText(in.nextString());
        }
    }

}
