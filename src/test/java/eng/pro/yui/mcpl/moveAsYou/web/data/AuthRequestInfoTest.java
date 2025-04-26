package eng.pro.yui.mcpl.moveAsYou.web.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.auth.TokenText;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestInfoTest {

    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    
    TokenText t1 = new TokenText("token1-token1-token1");
    TokenText t2 = new TokenText("token2-token2-token2");
    String p1 = "player1";
    String p2 = "player2";
    
    @Test
    void testConstruct(){
        assertDoesNotThrow(()-> new AuthRequestInfo(t1, p1));
    }
    
    @Test
    void testEquals(){
        assertTrue(new AuthRequestInfo(t1, p1).equals(new AuthRequestInfo(t1, p1)));
    }
    @Test
    void testNotEquals(){
        assertFalse(new AuthRequestInfo(t1, p2).equals(new AuthRequestInfo(t2, p2)));
    }
    
    @Test
    void testFromJson(){
        
        AuthRequestInfo instance = gson.fromJson(
                "{\"token\":\"token1-token1-token1\",\"playerName\":\"player1\"}",
                AuthRequestInfo.class);
        assertEquals(instance, new AuthRequestInfo(t1, p1));
    }
    
    
    

}