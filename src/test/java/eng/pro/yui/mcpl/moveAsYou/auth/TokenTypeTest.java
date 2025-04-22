package eng.pro.yui.mcpl.moveAsYou.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTypeTest {
    
    @Test
    void testGet_ByName(){
        assertEquals(TokenType.ONE_TIME, TokenType.get("ONE_TIME"));
        assertEquals(TokenType.STREAMING, TokenType.get("STREAMING"));
        assertEquals(TokenType.ADMIN, TokenType.get("ADMIN"));

        assertEquals(TokenType.ONE_TIME, TokenType.get("one_time"));
        assertEquals(TokenType.STREAMING, TokenType.get("streaming"));
        assertEquals(TokenType.ADMIN, TokenType.get("admin"));
    }
    @Test
    void testGet_ByAlias(){
        assertEquals(TokenType.ONE_TIME, TokenType.get("ONETIME"));
        assertEquals(TokenType.STREAMING, TokenType.get("STREAM"));
        assertEquals(TokenType.ADMIN, TokenType.get("ADMIN"));

        assertEquals(TokenType.ONE_TIME, TokenType.get("onetime"));
        assertEquals(TokenType.STREAMING, TokenType.get("stream"));
        assertEquals(TokenType.ADMIN, TokenType.get("admin"));
    }

}