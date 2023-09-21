package test.wordle;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wordle.*;
import test.Constants;
import test.Functions;
import test.Test;

public class KStartTest extends Test {
    List<String> l1 = Constants.l1;
    List<String> l3 = Constants.l3;
    List<String> fiveAns = Constants.fiveAns;
    List<String> fiveAllowed = Constants.fiveAllowed;
    KStartE k = new KStartE(fiveAllowed, fiveAns, 5, 2);

    @Override
    public void run() {
        super.test(() -> {
            // System.out.println(k.solve(5, 5, 5));
            Pair<NestedMap<Integer, String, List<String>>, Integer> p = k.solve(5,
                    Functions.sizeNested, 1, 1);
            System.out.println(p);

            // to print the output to output.txt
            Path output = Paths.get("output1.txt");
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
    }

}
