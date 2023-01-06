package test.wordle;

import java.util.List;

import test.Constants;
import wordle.*;

public class CheckTest {
    public void run() {
        List<String> l0 = List.of("round", "wound", "bound", "sound");
        Wordle w0 = new Wordle(l0, l0, 5);
        System.out.println(w0.check("round", l0));
        // expected: {240=[the others], 242=[round]}

        List<String> l1 = List.of("growl", "prowl", "grrrr", "rproo");
        Wordle w1 = new Wordle(l1, l1, 5);
        System.out.println(w1.check("rproo", List.of("growl", "prowl", "grrrr")));
        // expected: {21=[grrrr], 12=[growl], 13=[prowl]}

        List<String> l2 = List.of("moral", "coral", "royal",
        "rival", "flora", "mural", "lycra", "rural", "viral", "aural");
        Wordle w2 = new Wordle(l2, l2, 5);
        
        System.out.println(w2.check("moral", l2));
        // {240=[coral], 242=[moral], 120=[flora], 217=[rival], 234=[rural, viral, aural], 236=[mural], 109=[lycra], 223=[royal]}
        System.out.println(w2.check("coral", l2));
        // {240=[moral], 242=[coral], 118=[lycra], 120=[flora], 217=[rival], 234=[mural, rural, viral, aural], 223=[royal]}

        
        
        // Speed tests here
        // So this is still faster by ~3 times
        Wordle w3 = new Wordle(Constants.fiveAns, Constants.fiveAns, 5);
        long start = System.nanoTime();
        for (int i = 0; i < Constants.fiveAns.size(); i++) {
            w3.check(Constants.fiveAns.get(i), Constants.fiveAns);
        }
        long end = System.nanoTime();
        System.out.println((end - start)*Math.pow(10, -9));


    }
}
