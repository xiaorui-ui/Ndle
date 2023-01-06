package test.wordle;

import test.Test;
import wordle.*;

public class CompareTest extends Test {
    @Override
    public void run() {
        Test test = new CompareTest();
        // The following tests whether compare is implemented correctly
        // Function compares 2nd word against the first
        test.<Integer>equals(Wordle.compare("sassy", "sassy", 5), 242);
        // expected = 2,2,2,2,2 so 242

        test.<Integer>equals(Wordle.compare("round", "salet", 5), 0);
        // expected = 0,0,0,0,0 so 0

        test.<Integer>equals(Wordle.compare("brrrr", "round", 5), 3);
        // expected = 0,1,0,0,0 so 3

        test.<Integer>equals(Wordle.compare("sassy", "asset", 5), 22);
        // expected = 1,1,2,0,0 so 22

        test.<Integer>equals(Wordle.compare("asset", "sassy", 5), 22);
        // expected = 1,1,2,0,0 so 22

        test.<Integer>equals(Wordle.compare("robot", "opium", 5), 3);
        // expected = 0,1,0,0,0 so 3

        test.<Integer>equals(Wordle.compare("asshs", "shshh", 5), 75);
        // expected = 0,1,2,2,0 so 75

        test.<Integer>equals(Wordle.compare("shshh", "asshh", 5), 235);
        // expected = 1,0,2,2,2 so 235
        
    }
}
