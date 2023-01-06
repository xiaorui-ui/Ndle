package test.dordle;

import java.util.List;

import dordle.*;
import wordle.*;
import test.Constants;
import test.Functions;
import test.Test;


public class SolveTest extends Test {
    @Override
    public void run() {
        List<String> l = List.of("aaaaa", "baaaa", "caaaa", "daaaa");
        List<String> l2 = Constants.l2;
        List<String> l3 = Constants.l3;
        List<String> Short = Constants.fiveAns.subList(0, 500);
        List<String> Shorter = Constants.fiveAns.subList(0, 500);
        List<String> test = List.of();
        Dordle d = new Dordle(Constants.fiveAllowed, Shorter, 5);
        super.test(() -> {
            // closed form for non-separating lists of n elements (n-1)n(2n+2)/6            
            return new DordleGeneric(l2, l2, 5, 5).solve(l2, Functions.size);
        });

        // I REALLY need to test for correctness LOL, with smaller lists first

        super.test(() -> {
            return new WordleMemo(l3, l3, 5).solve(l3, 5);
        });
        
        super.test(() -> {

            return new DordleGeneric(l3, l3, 5, 1).solve(l3, Functions.size);
        });


        // super.test(() -> {
        //     return new DordleGeneric(Constants.get5Allowed(), Shorter, 5).solve(Shorter, Functions.qSum);
        // });

        super.test(() -> 
        {
            return d.solve(Shorter);
        });

        // ~ 80 seconds

        // super.test(() -> 
        // {
        //     return new Dordle(Constants.get5Allowed(), Shorter, 5).solve(Shorter);
        // });

        

        
    }
    
}
