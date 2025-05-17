package eng.pro.yui.mcpl.moveAsYou.consts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BgColorTest {
    
    @Test
    void runGetByStr(){
        assertEquals(BgColor.BLUE, BgColor.get("blue"));
    }

}