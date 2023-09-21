// System.gc() to clear the garbage

package test.wordle;

import java.util.List;

import test.Constants;
import test.Test;
import wordle.*;

public class SolveTest extends Test {
    @Override
    public void run() {
        List<String> fiveAns = Constants.fiveAns;
        List<String> fiveAllowed = Constants.fiveAllowed;
        List<String> nyt = Constants.nyt;
        List<String> nytAllowed = Constants.nytAllowed;

        // Sample code for easy copy-paste
        // Instant s = Instant.now();
        // Wordle w0 = new Wordle(fiveAns, fiveAns, 5);
        // Instant e = Instant.now();
        // System.out.println(Duration.between(s, e));

        // Wordle w0 = new Wordle(nytAllowed, fiveAns, 5);

        long t1 = System.nanoTime();
        Wordle w0 = new Wordle(fiveAllowed, fiveAns, 5);
        long t2 = System.nanoTime();
        System.out.println((t2 - t1) * Math.pow(10, -9));

        // Thread t = new Thread(() -> {
        // super.test(() -> {return w0.solve(fiveAns, 5);});
        // });
        //
        // t.start();

        super.test(() -> {
            return w0.solve(fiveAns, 5);
        });

        // while (t.isAlive()) {
        // try {
        // Thread.sleep(1000);
        // System.out.println(".");
        // Thread.sleep(1000);
        // System.out.println(",");
        // } catch (InterruptedException i) {
        // System.out.println("x");
        // }
        // }

        List<String> l1 = Constants.l1;
        List<String> l2 = Constants.l2;
        List<String> l3 = Constants.l3;

        super.test(() -> {
            Wordle w1 = new Wordle(l1, l1, 5);
            return w1.solve(l1, 5);
        });

        super.test(() -> {
            Wordle w1 = new Wordle(l2, l2, 5);
            return w1.solve(l2, 5);
        });

        super.test(() -> {
            Wordle w1 = new Wordle(l3, l3, 5);
            return w1.solve(l3, 5);
        });

        super.test(() -> {
            Wordle w1 = new Wordle(l1, l2, 5);
            return w1.solve(l2, 5);
        });

    }

}
