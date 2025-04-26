package eng.pro.yui.mcpl.moveAsYou.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTextTest {
    
    @Test
    void testEquals(){
        assertEquals(new TokenText("token1-token2-token3"), new TokenText("token1-token2-token3"));
        assertNotEquals(new TokenText("token1-token2-token3"), new TokenText("token3-token2-token1"));
    }

}