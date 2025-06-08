package eng.pro.yui.mcpl.moveAsYou.web.data;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class IndexPlayerTest {
    
    @Test
    void testFull(){
        Calendar t1 = Calendar.getInstance();
        t1.set(2025, Calendar.JANUARY, 1, 0, 0, 0);
        Calendar t2 = Calendar.getInstance();
        t2.set(2025, Calendar.JANUARY, 1, 23, 59, 58);
        
        assertEquals("23時間59分58秒", IndexPlayer.getLength(t2.getTimeInMillis(), t1.getTimeInMillis()));
    }

    @Test
    void testSingleDigits() {
        Calendar t1 = Calendar.getInstance();
        t1.set(2025, Calendar.JANUARY, 1, 0, 0, 0);
        Calendar t2 = Calendar.getInstance();
        t2.set(2025, Calendar.JANUARY, 1, 1, 5, 7);

        assertEquals("1時間05分07秒", IndexPlayer.getLength(t2.getTimeInMillis(), t1.getTimeInMillis()));
    }

    @Test
    void testThreeDigitHours() {
        Calendar t1 = Calendar.getInstance();
        t1.set(2025, Calendar.JANUARY, 1, 0, 0, 0);
        Calendar t2 = Calendar.getInstance();
        t2.set(2025, Calendar.JANUARY, 6, 12, 0, 0);

        assertEquals("132時間00分00秒", IndexPlayer.getLength(t2.getTimeInMillis(), t1.getTimeInMillis()));
    }

    @Test
    void testOneHourZeroMinutes() {
        Calendar t1 = Calendar.getInstance();
        t1.set(2025, Calendar.JANUARY, 1, 0, 0, 0);
        Calendar t2 = Calendar.getInstance();
        t2.set(2025, Calendar.JANUARY, 1, 1, 0, 2);

        assertEquals("1時間00分02秒", IndexPlayer.getLength(t2.getTimeInMillis(), t1.getTimeInMillis()));
    }

    @Test
    void testZeroHoursFiftyNineSeconds() {
        Calendar t1 = Calendar.getInstance();
        t1.set(2025, Calendar.JANUARY, 1, 0, 0, 0);
        Calendar t2 = Calendar.getInstance();
        t2.set(2025, Calendar.JANUARY, 1, 0, 0, 59);

        assertEquals("59秒", IndexPlayer.getLength(t2.getTimeInMillis(), t1.getTimeInMillis()));
    }


}