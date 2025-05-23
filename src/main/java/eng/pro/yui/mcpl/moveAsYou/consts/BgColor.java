package eng.pro.yui.mcpl.moveAsYou.consts;

import java.util.ArrayList;
import java.util.List;

public enum BgColor {
    GREEN(0x00ff80),
    BLUE(0x0080ff),
    RED(0xff0000);
    
    public final int color;
    public int getColorCode(){
        return this.color;
    }
    
    BgColor(int color){
        if(color < 0 || 0xffffff < color ) {
            throw new IllegalArgumentException("color must be between 0 and 0xffffff");
        }
        this.color = color;
    }
    
    public static BgColor get(int color){
        for(BgColor c : BgColor.values()){
            if(c.color == color){
                return c;
            }
        }
        throw new IllegalArgumentException("color not defined");
    }
    public static BgColor get(String name){
        if(name == null){ return null; }
        String input = name.toUpperCase();
        for(BgColor c : BgColor.values()){
            if(c.name().equals(input)){
                return c;
            }
        }
        throw new IllegalArgumentException("color not defined");
    }
    
    public static List<String> names(){
        List<String> result = new ArrayList<>();
        for(BgColor c : BgColor.values()) {
            result.add(c.name());
        }
        return result;
    }
    
    
}
