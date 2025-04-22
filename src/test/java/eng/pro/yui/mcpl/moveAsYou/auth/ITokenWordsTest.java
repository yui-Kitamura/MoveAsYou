package eng.pro.yui.mcpl.moveAsYou.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ITokenWordsTest {

    @Test
    public void getCount() {
        System.out.println("getCount -->");
        int min, max;
        System.out.print("COMMON_ITEMS: ");
        min = Integer.MAX_VALUE; max = 0;
        for(String s: ITokenWords.COMMON_ITEMS) {
            min = Math.min(min, s.length());
            max = Math.max(s.length(), max);
        }
        assertTrue(3 < min);
        assertTrue(max < 10);
        System.out.printf("%d～%d, size=%d%n",min,max, ITokenWords.COMMON_ITEMS.size());
        
        System.out.print("SPECIAL_ITEMS: ");
        min = Integer.MAX_VALUE; max = 0;
        for(String s: ITokenWords.SPECIAL_ITEMS) {
            min = Math.min(min, s.length());
            max = Math.max(s.length(), max);
        }
        assertTrue(3 < min);
        assertTrue(max < 10);
        System.out.printf("%d～%d, size=%d%n",min,max, ITokenWords.SPECIAL_ITEMS.size());
        
        System.out.print("MODIFIERS: ");
        min = Integer.MAX_VALUE; max = 0;
        for(String s: ITokenWords.MODIFIERS) {
            min = Math.min(min, s.length());
            max = Math.max(s.length(), max);
        }
        assertTrue(3 < min);
        assertTrue(max < 10);
        System.out.printf("%d～%d, size=%d%n",min,max, ITokenWords.MODIFIERS.size());
    }
}