package wordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class KStartE extends WordleMemo {

    // denotes the number of words to pre-guesses(1 is your typical Wordle)
    private int k;

    public KStartE(List<String> allowed, List<String> ans, int l, int k) {
        super(allowed, ans, l);
        this.k = k;
    }

    // provided l is a subset of this.allowed
    public HashMap<Long, List<String>> check(String s) {
        List<String> l = Arrays.asList(s.split(","));
        HashMap<Long, List<String>> h = new HashMap<>();
        for (int i = 0; i < this.ans.size(); i++) {
            long key = 0;
            for (int j = 0; j < l.size(); j++) {
                key += Math.pow(3, this.l*j) * this.compare.get(l.get(j)).get(this.ans.get(i));
                // key += Math.pow(3, this.l*j)*Wordle.compare(l.get(j), this.ans.get(i), this.l);
            }
            if (!h.containsKey(key)) {
                h.put(key, new ArrayList<String>());
            }
            h.get(key).add(this.ans.get(i));
        }
        return h;
    }

    // the smaller the better!
    public double entropy(HashMap<?, ? extends List<?>> h) {
        double d = 0;
        for (Object x: h.keySet()) {
            int a = h.get(x).size();
            d += Math.log(a)*a;
        }
        return d;
    }

    // Not overriding
    public List<String> succ(String ss, int t) {
        int x = this.allowed.size();
        // int y = this.ans.size();

        List<Pair<String, Double>> groups = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            HashMap<Long, List<String>> h;
            if (ss != "") {
                h = this.check(ss + "," + s);
            } else {
                h = this.check(s);
            }
            
            // if (h.size() == y) {
            //     return List.of(s);
            // }
            groups.add(new Pair<>(s, this.entropy(h)));            
        }

        Collections.sort(groups, (z, zz) -> {
            // return zz.getSnd() - z.getSnd();
            if (zz.getSnd() > z.getSnd()) {
                return -1;
            }
            return 1;
        });


        double threshold = groups.get(Math.min(t, groups.size() - 1)).getSnd();
        List<String> li = new ArrayList<>();
        int i = 0;
        while (i < x && groups.get(i).getSnd() <= threshold) {
            li.add(groups.get(i).getFst());
            i++;
        }

        // System.out.println(li);

        return li;
    }


    // breadth at each stage as specified, unspecified => 1
    // How to account for solve?
    // Recursively divide by 243 and sth sth modify LOL
    public List<String> start(int ... breadth) {
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
            for (String s: ans) {
                List<String> l = this.succ(s, m);
                for (String ss: l) {
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

    public Pair<Tree<String, Long>, Integer> solve(int w, int ... breadth) {
        List<String> starts = this.start(breadth);
        // System.out.println(starts);
        Pair<Tree<String, Long>, Integer> p = starts.stream().map(x -> this.miniSolveMemo(x, w)).parallel()
        .reduce(new Pair<Tree<String, Long>, Integer>
        (null, Integer.MAX_VALUE), (z, aa) -> (z.getSnd() < aa.getSnd()) ? z : aa);
        return p;

    }

    // no override as parameter types aren't the same
    public Pair<Tree<String, Long>, Integer> miniSolveMemo(String s, int x) {
        HashMap<Long, List<String>> h = this.check(s);
        Tree<String, Long> t = new Tree<>(s);
        int sum = 0;
        for (long j: h.keySet()) {
            Pair<Tree<String, Long>, Integer> p1 = super.solve(h.get(j), x);
            sum += p1.getSnd();
            t.put(j, p1.getFst());
        }
        // I just won't account for the solve case first LOL
        sum += this.k * this.ans.size();
        return new Pair<>(t, sum);

    }

    
}
