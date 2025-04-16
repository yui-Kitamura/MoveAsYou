package eng.pro.yui.mcpl.moveAsYou.consts;

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
    
    
}
