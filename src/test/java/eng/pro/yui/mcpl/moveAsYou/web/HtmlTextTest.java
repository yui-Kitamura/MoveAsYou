package eng.pro.yui.mcpl.moveAsYou.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlTextTest {
    
    @Test
    void testShortString(){
        HtmlText html = HtmlText.get("loooooongTitlE", "loooooooooooooonnnnnnnnnnnnnngBodY");
        String out = html.toShortString();
        assertEquals(100, out.length());
    }
    @Test
    void testShortString2(){
        HtmlText htmlText = HtmlText.get("short", "short");
        String out = htmlText.toShortString();
        String expected = "<!DOCTYPE html><html lang=\"ja\"><title>short|null</title><body>short</body></html>";
        assertEquals(expected.length(), out.length());
    }

}