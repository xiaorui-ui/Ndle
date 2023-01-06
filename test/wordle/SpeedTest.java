package test.wordle;


import java.util.HashMap;
import java.util.List;

import test.Constants;
import test.Test;
import wordle.*;

public class SpeedTest extends Test {
    @Override
    public void run() {
        // correctness still there, class method slower
        List<String> fiveAns = Constants.fiveAns;
        List<String> fiveAllowed = Constants.fiveAllowed; 

        // Wordle w1 = new Wordle(fiveAllowed, fiveAns, 5);
        // This is faster than the 2nd as expected, by about 10 times
        // so why is the solve method slower than the static solve method???
        HashMap<String, HashMap<String, Integer>> h = Wordle.hash(fiveAllowed, fiveAns, 5);
        long start = System.nanoTime();
        for (int i = 0 ; i < fiveAllowed.size(); i++) {
            for (int j = 0 ; j < fiveAns.size(); j++) {
                // Integer k = h.get(fiveAllowed.get(i)).get(fiveAns.get(j));
                int k = h.get(fiveAllowed.get(i)).get(fiveAns.get(j));
            }
        }
        long end  = System.nanoTime();
        System.out.println((end - start)*Math.pow(10, -9));

        // Slightly slower than the previous due to the need to access attribute
        // Wordle w = new Wordle(fiveAllowed, fiveAns, 5);
        // start = System.nanoTime();
        // for (int i = 0 ; i < fiveAllowed.size(); i++) {
        //     for (int j = 0 ; j < fiveAns.size(); j++) {
        //         int k = w.compare.get(fiveAllowed.get(i)).get(fiveAns.get(j));
        //     }
        // }
        // end  = System.nanoTime();
        // System.out.println((end - start)*Math.pow(10, -9));
        
        start = System.nanoTime();
        for (int i = 0 ; i < fiveAllowed.size(); i++) {
            for (int j = 0 ; j < fiveAns.size(); j++) {
                int k = Wordle.compare(fiveAllowed.get(i), fiveAns.get(j), 5);
            }
        }
        end  = System.nanoTime();
        System.out.println((end - start)*Math.pow(10, -9));

        // This is slower than the 2nd, possibly due to the large memory? Like, very slow.
        // HashMap<String, Integer> h2 = Wordle.hash2(fiveAllowed, fiveAns, 5);
        // start = System.nanoTime();
        // for (int i = 0 ; i < fiveAllowed.size(); i++) {
        //     for (int j = 0 ; j < fiveAns.size(); j++) {
        //         Integer k = h2.get(fiveAllowed.get(i) + fiveAns.get(j));
        //     }
        // }
        // end  = System.nanoTime();
        // System.out.println((end - start)*Math.pow(10, -9));

        start = System.nanoTime();
        for (int i = 0 ; i < fiveAllowed.size(); i++) {
            for (int j = 0 ; j < fiveAns.size(); j++) {
                int k = Wordle.compare(fiveAllowed.get(i), fiveAns.get(j), 5);
            }
        }
        end  = System.nanoTime();
        System.out.println((end - start)*Math.pow(10, -9));


        List<String> l1 = Constants.l1;
        List<String> l2 = Constants.l2;

        start = System.nanoTime();
        for (int g = 0; g < 1000; g++) {
            for (int i = 0 ; i < l1.size(); i++) {
                for (int j = 0 ; j < l1.size(); j++) {
                    int k = Wordle.compare(l1.get(i), l1.get(j), 5);
                }
            }
        }        
        end  = System.nanoTime();
        System.out.println((end - start)*Math.pow(10, -9));

        h = Wordle.hash(l1, l1, 5);
        start = System.nanoTime();
        for (int g = 0; g < 1000; g++) {
            for (int i = 0 ; i < l1.size(); i++) {
                for (int j = 0 ; j < l1.size(); j++) {
                    int k = h.get(l1.get(i)).get(l1.get(j));
                }
            }
        }        
        end  = System.nanoTime();
        System.out.println((end - start)*Math.pow(10, -9));


    }

    

    
}
