package eng.pro.yui.mcpl.moveAsYou.auth;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TokenText {
    
    private static final String DELIMITER = "-";
    
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
    
    @Contract(" -> new")
    public static @NotNull TokenText generate(){
        return new TokenText("test-token-that-valid"); //TODO use itemName list
    }
    
    public static boolean isValid(String applicant){
        if(applicant == null || applicant.isBlank()){
            return false;
        }

        String[] parts = applicant.split(DELIMITER);
        if (parts.length < 3 || 4 < parts.length) {
            return false;
        }
        for (String part : parts) {
            if (part.length() < 3 || 7 < part.length()) {
                return false;
            }
        }
        return true;
    }

    private final static List<String> COMMON_ITEMS = List.of(
            "acacia", "amethyst", "andesite", "birch", "brick",
            "cactus", "clay", "coal", "cobble", "copper", "diamond",
            "diorite", "emerald", "glass", "glowstone", "gold",
            "granite", "iron", "jungle", "lapis", "leather",
            "obsidian", "paper", "quartz",
            "redstone", "slime", "spruce", "stone", "wool"
    );

    private final static List<String> SPECIAL_ITEMS = List.of(
            "bamboo", "beacon", "blaze", "chorus",
            "cherry", "elytra", "ender", "heart",
            "honey", "nether", "phantom", "prism",
            "totem", "trident", "turtle", "wither"

    );

    private final static List<String> MODIFIERS = List.of(
            "broken", "dark", "enchanted", "flaming", "frozen",
            "glowing", "golden", "lucky", "mystic", "royal", "shiny"
    );

    private static List<String> all(){
        List<String> all = new ArrayList<>(COMMON_ITEMS);
        all.addAll(MODIFIERS);
        all.addAll(SPECIAL_ITEMS);
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
}
