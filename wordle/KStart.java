package wordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class KStart extends WordleMemo {

    // denotes the number of words to pre-guesses(1 is your typical Wordle)
    private int k;

    public KStart(List<String> allowed, List<String> ans, int l, int k) {
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


    // breadth at each stage as specified, unspecified => 1
    // How to account for solve?
    // Recursively divide by 243 and sth sth modify LOL
    public List<String> start(int ... breadth) {
        int sz = this.allowed.size();
        if (breadth.length > this.k) {
            throw new IllegalArgumentException("Too many arguments, require <= this.k arguments");
        }

        List<String> ans = new ArrayList<>();
        for (int i = 0; i < this.k; i++) {
            if (i == 0) {
                if (breadth.length > 0) {
                    ans = this.succ(this.ans, breadth[0]);
                } else {
                    ans = this.succ(this.ans, 1);
                }
                
            } else {
                List<String> ll = new ArrayList<>();
                for (String s: ans) {
                    List<Pair<String, Integer>> l = new ArrayList<>();
                    for (int j = 0; j < this.allowed.size(); j++) {
                        String temp = s + "," + this.allowed.get(j);
                        int size = this.check(temp).size();
                        l.add(new Pair<>(temp, size));
                    }
                    
                    Collections.sort(l, (z, zz) -> {
                        return zz.getSnd() - z.getSnd();
                    });


                    int m;
                    if (breadth.length > i) {
                        m = breadth[i];
                    } else {
                        m = 1;
                    }
                    int threshold = l.get(Math.min(m, l.size() - 1)).getSnd();
                    List<String> li = new ArrayList<>();
                    int j = 0;
                    while (j < sz && l.get(j).getSnd() >= threshold) {
                        li.add(l.get(j).getFst());
                        j++;
                    }
            
                    ll.addAll(li);
                } 
                ans = ll;               
            }                                          
        }
        return ans;
    }

    public Pair<Tree<String, Long>, Integer> solve(int w, int ... breadth) {
        List<String> starts = this.start(breadth);
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
