package eng.pro.yui.mcpl.moveAsYou.auth;

import java.util.ArrayList;
import java.util.List;

public interface ITokenWords {
    List<String> COMMON_ITEMS = List.of(
            "acacia", "amethyst", "andesite", "birch", "brick",
            "cactus", "clay", "coal", "cobble", "copper", "diamond",
            "diorite", "emerald", "glass", "glowstone", "gold",
            "granite", "iron", "jungle", "lapis", "leather",
            "obsidian", "paper", "quartz",
            "redstone", "slime", "spruce", "stone", "wool"
    );

    List<String> SPECIAL_ITEMS = List.of(
            "bamboo", "beacon", "blaze", "chorus",
            "cherry", "elytra", "ender", "heart",
            "honey", "nether", "phantom", "prism",
            "totem", "trident", "turtle", "wither"

    );

    List<String> MODIFIERS = List.of(
            "broken", "dark", "enchanted", "flaming", "frozen",
            "glowing", "golden", "lucky", "mystic", "royal", "shiny"
    );
    
    static List<String> all(){
        List<String> all = new ArrayList<>(COMMON_ITEMS);
        all.addAll(MODIFIERS);
        all.addAll(SPECIAL_ITEMS);
        return all;
    }



}
