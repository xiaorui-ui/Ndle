package wordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class KStartE extends WordleMemo {

    // denotes the number of words to pre-guesses(1 is your typical Wordle)
    private int k;

    public KStartE(List<String> allowed, List<String> ans, int l, int k) {
        super(allowed, ans, l);
        this.k = k;
    }

    // recursive
    // assume s nonempty
    public NestedMap<Integer, String, List<String>> checkKStart(String s, List<String> l) {
        List<String> ll = Arrays.asList(s.split(","));
        if (ll.size() == 1) {
            NestedMap<Integer, String, List<String>> nm = new NestedMap<>(s, l);
            HashMap<Integer, List<String>> h = this.check(s, l);
            for (Integer i : h.keySet()) {
                nm.put(i);
                if (i != c) {
                    nm.get(i).setW(h.get(i));
                } else {
                    nm.replace(c, null);
                }
            }
            return nm;
        }
        String word = ll.get(0);
        int len = word.length();
        String t = s.substring(len + 1);
        NestedMap<Integer, String, List<String>> nm = new NestedMap<>(word, l);
        HashMap<Integer, List<String>> h = this.check(word, l);
        for (Integer i : h.keySet()) {
            if (i != c) {
                nm.put(i, this.checkKStart(t, h.get(i)));
            } else {
                nm.put(i, null);
            }
        }
        return nm;

    }

    // Not overriding
    public List<String> succ(String ss, int t, Function<NestedMap<Integer, String, List<String>>, ? extends Number> f) {
        int x = this.allowed.size();
        // int y = this.ans.size();

        List<Pair<String, ? extends Number>> groups = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            NestedMap<Integer, String, List<String>> nm;
            if (ss != "") {
                nm = this.checkKStart(ss + "," + s, this.ans);
            } else {
                nm = this.checkKStart(s, this.ans);
            }

            // if (h.size() == y) {
            // return List.of(s);
            // }
            groups.add(new Pair<>(s, f.apply(nm)));
        }

        Collections.sort(groups, (z, zz) -> {
            // return zz.getSnd() - z.getSnd();
            if (z.getSnd().floatValue() > zz.getSnd().floatValue()) {
                return 1;
            }
            return -1;
        });

        float threshold = groups.get(Math.min(t, groups.size() - 1)).getSnd().floatValue();
        List<String> li = new ArrayList<>();
        int i = 0;
        while (i < x && groups.get(i).getSnd().floatValue() <= threshold) {
            li.add(groups.get(i).getFst());
            i++;
        }

        // System.out.println(li);

        return li;
    }

    // breadth at each stage as specified, unspecified => 1
    // How to account for solve?
    // Recursively divide by 243 and sth sth modify
    public List<String> start(Function<NestedMap<Integer, String, List<String>>, ? extends Number> f,
            int... breadth) {
        if (breadth.length > this.k) {
            throw new IllegalArgumentException("Too many arguments, require <= this.k arguments");
        }

        List<String> ans = new ArrayList<>();
        ans.add("");
        for (int i = 0; i < this.k; i++) {
            List<String> temp = new ArrayList<>();
            int m;
            if (breadth.length <= i) {
                m = 1;
            } else {
                m = breadth[i];
            }
            for (String s : ans) {
                List<String> l = this.succ(s, m, f);
                for (String ss : l) {
                    if (s != "") {
                        temp.add(s + "," + ss);
                    } else {
                        temp.add(ss);
                    }

                }
            }
            // System.out.println(temp);
            ans = temp;
        }
        return ans;
    }

    public Pair<NestedMap<Integer, String, List<String>>, Integer> solve(int w,
            Function<NestedMap<Integer, String, List<String>>, ? extends Number> f, int... breadth) {
        List<String> starts = this.start(f, breadth);
        // System.out.println(starts);
        Pair<NestedMap<Integer, String, List<String>>, Integer> p = starts.stream()
                .map(x -> this.miniSolveKStart(x, this.ans, w)).parallel()
                .reduce(new Pair<NestedMap<Integer, String, List<String>>, Integer>(null, Integer.MAX_VALUE),
                        (z, aa) -> (z.getSnd() < aa.getSnd()) ? z : aa);
        return p;

    }

    public Pair<NestedMap<Integer, String, List<String>>, Integer> miniSolveKStart(String s, List<String> l, int x) {
        List<String> ll = Arrays.asList(s.split(","));
        if (ll.size() == 1) {
            int sum = 0;
            NestedMap<Integer, String, List<String>> nm = this.checkKStart(s, l);
            for (Integer i : nm.keySet()) {
                if (i == c) {
                    continue;
                }
                Pair<NestedMap<Integer, String, List<String>>, Integer> p = super.solve(nm.get(i).getW(), x);
                nm.replace(i, p.getFst());
                sum += p.getSnd();
            }
            sum += l.size();
            return new Pair<>(nm, sum);
        }

        int ans = 0;

        String word = ll.get(0);
        int len = word.length();
        String t = s.substring(len + 1);
        NestedMap<Integer, String, List<String>> nm = new NestedMap<>(word, null);
        HashMap<Integer, List<String>> h = this.check(word, l);
        for (Integer i : h.keySet()) {
            if (i == c) {
                nm.put(c, null);
            } else {
                Pair<NestedMap<Integer, String, List<String>>, Integer> p = this.miniSolveKStart(t, h.get(i), x);
                nm.put(i, p.getFst());
                ans += p.getSnd();
            }

        }

        ans += l.size();

        return new Pair<>(nm, ans);
    }

}
