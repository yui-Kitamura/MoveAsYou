package eng.pro.yui.mcpl.moveAsYou.consts;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BgColorTest {
    
    @Test
    void runGetByStr(){
        assertEquals(BgColor.BLUE, BgColor.get("blue"));
    }
    @Test
    void runGetByStrMixed(){
        assertEquals(BgColor.GREEN, BgColor.get("GREen"));
    }
    @Test
    void runGetByInt(){
        assertEquals(BgColor.RED, BgColor.get(0xff0000));
    }
    
    @Test
    void nameList(){
        List<String> names = BgColor.names();
        assertAll(
                ()->assertFalse(names.isEmpty()),
                ()->assertEquals(BgColor.values().length, names.size())
        );
    }

}