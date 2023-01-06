package test.wordle;

import java.util.List;

import test.Constants;
import test.Test;
import wordle.*;

public class SuccTest extends Test {
    @Override
    public void run() {
        List<String> ans = Constants.fiveAns;
        List<String> a = Constants.fiveAllowed;
        Wordle w = new Wordle(ans, ans, 5);
        

        // super.test(() -> {System.out.println(Wordle.succ(ans, ans)); return null;});

        super.test(() -> {System.out.println(w.succ(ans, 5).stream().toList()); return null;});
        
    }
    
}
