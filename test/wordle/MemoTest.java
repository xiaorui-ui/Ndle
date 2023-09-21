package test.wordle;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import test.Constants;
import test.Test;
import wordle.*;

public class MemoTest extends Test {
    @Override
    public void run() {
        // Results may be inconsistent due to parallelism oops
        List<String> l1 = Constants.l1;
        List<String> l3 = Constants.l3;
        WordleMemo w1 = new WordleMemo(l1, l1, 5);
        WordleMemo w2 = new WordleMemo(Constants.fiveAllowed, Constants.fiveAns, 5);
        WordleMemo w3 = new WordleMemo(l3, l3, 5);
        System.out.println(w1.solve(l1, 5));
        System.out.println(w3.solve(l3, 5).getFst().get(234).toString() + w3.solve(l3, 5));
        // w2.print(Constants.fiveAns);
        // new Wordle(Constants.fiveAllowed, Constants.fiveAns,
        // 5).print(Constants.fiveAns);
        super.test(
                () -> {
                    // WordleMemo w = new WordleMemo(Constants.nyt, Constants.nytAllowed, 5);
                    Pair<NestedMap<Integer, String, List<String>>, Integer> p = w2.solve(Constants.fiveAns, 5);
                    System.out.println(p);
                    // System.out.println(WordleMemo.print(p.getFst()));
                    // System.out.println(p.getFst().deepest());
                    // System.out.println(w2.solve(Constants.fiveAns).getFst().get(0).get(0));

                    // to print the output to output.txt
                    Path output = Paths.get("output.txt");
                    try {
                        List<String> l = new ArrayList<>();
                        List<List<String>> ll = WordleMemo.print(p.getFst());
                        Collections.sort(ll, (x, y) -> x.get(x.size() - 1).compareTo(y.get(y.size() - 1)));
                        for (int i = 0; i < ll.size(); i++) {
                            List<String> t = ll.get(i);
                            int j = t.size();
                            String s = "";
                            for (int k = 0; k < j; k++) {
                                if (k == j - 1) {
                                    s += t.get(k);
                                } else {
                                    s += t.get(k) + ",";
                                }
                            }
                            l.add(s);

                            // l.add(ll.get(i).toString());
                        }

                        Files.write(output, l, Charset.defaultCharset());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        super.test(
                () -> {
                    System.out.println(w2.solve(Constants.fiveAns, 5).getFst().deepest());
                    return null;
                });
        // super.test(
        // () -> {
        // Path output = Paths.get("output2.txt");
        // try {
        // List<String> l = new ArrayList<>();
        // for (int i = 0; i < Constants.fiveAns.size(); i++) {
        // l.add("salet," + Constants.fiveAns.get(i));
        // // l.add(ll.get(i).toString());
        // }

        // Files.write(output, l, Charset.defaultCharset());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return 1;
        // }
        // );

    }

}
