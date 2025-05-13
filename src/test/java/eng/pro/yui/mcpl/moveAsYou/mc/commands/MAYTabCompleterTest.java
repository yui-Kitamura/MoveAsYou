package eng.pro.yui.mcpl.moveAsYou.mc.commands;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MAYTabCompleterTest {

    MAYTabCompleter completer = new MAYTabCompleter();
    List<String> list = Arrays.asList("ABC", "abc", "xyz", "zyx");

    @Test
    void filterTest() {
        List<String> res = completer.filter(list,"a");
        assertAll(
                ()-> assertEquals(2, res.size()),
                ()-> assertEquals(Arrays.asList("ABC","abc"), res)
        );
    }
    @Test
    void filterTest2(){
        List<String> res = completer.filter(list, "x");
        assertEquals(Arrays.asList("xyz"), res);
    }
    @Test
    void filterTest3(){
        List<String> res = completer.filter(list, "");
        assertEquals(list, res);
    }
}