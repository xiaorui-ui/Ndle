package dordle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import wordle.*;

public class DordleGeneric extends Wordle {

    public DordleGeneric(List<String> allowed, List<String> ans, int l, int t) {
        super(allowed, ans, l);
    }

    // Return largest only + ties now
    // We can ignore non-separating case later because of this?
    // Also notice if all the partitions are <= size 2 we can return instantly
    public List<String> succ(List<String> l, 
    BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> f) {
        int x = this.allowed.size();
        int y = l.size();
        int min = Integer.MAX_VALUE;
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            HashMap<Integer, List<String>> h = this.check(s, l);
            if (f.apply(l, h) <= min) {
                if (f.apply(l, h) == y) {
                    return List.of(s);
                }
                if (f.apply(l, h) != min) {
                    min = f.apply(l, h);
                    ans = new ArrayList<>();
                }
                ans.add(s);                
            }
        }
        return ans;
    }

    // When the sets are disjoint
    public List<String> succ(List<String> l1, List<String> l2, 
    BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> f) {
        int x = this.allowed.size();
        // int y = l1.size();
        // int z = l2.size();
        int min = Integer.MAX_VALUE;
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            HashMap<Integer, List<String>> h1 = this.check(s, l1);
            HashMap<Integer, List<String>> h2 = this.check(s, l2);
            int prod = f.apply(l1, h1) * f.apply(l2, h2);

            if (prod <= min) {
                // if (prod == y*z) {
                //     return List.of(s);
                // }
                if (prod != min) {
                    min = prod;
                    ans = new ArrayList<>();
                }
                ans.add(s);
            }
        }
        return ans;
    }

    // @Override
    public Pair<?, Integer> solve(List<String> l, 
    BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> f) {
        int x = l.size();
        if (x <= 2) {
            return new Pair<>(l.get(0), 2*x - 2);
        }
        // happy case
        if (x < 20) {
            Pair<?, Integer> p = l.stream().map(w -> new Pair<>(w, this.miniSolve(w, l, f)))
            .reduce(new Pair<>(null, 4*x*x), (y, z) -> (y.getSnd() < z.getSnd()) ? y : z);
            if (p.getSnd() <= 3*x*(x - 1)/2) {
                return p;
            }                      
        }

        List<String> succ = this.succ(l, f);
        return succ.stream().map(w -> new Pair<>(w, this.miniSolve(w, l, f)))
        .reduce(new Pair<>(null, Integer.MAX_VALUE), (y, z) -> (y.getSnd() < z.getSnd()) ? y : z);
    }

    // @Override
    public int miniSolve(String word, List<String> ans, 
    BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> f) {
        int s = ans.size();
        HashMap<Integer, List<String>> h = this.check(word, ans);
        // if (h.size() == 1) {
        //     return Integer.MAX_VALUE;
        // } 

        // if (h.size() == s) {
        //     return 3*s*(s-1)/2;
        // }
        
        int sum = 0;
        for (int i : h.keySet()) {
            if (i == c) {
                // The minisolve will reference !this solve method instead!
                // sum += super.miniSolve(word, ans) - ans.size();
                for (int j : h.keySet()) {
                    sum += super.solve(h.get(j), 1).getSnd();
                }
                // System.out.println(sum);
                sum -= 1;

            } else {
                for (int j : h.keySet()) {
                    if (j < i) {
                        sum += this.solve(h.get(i), h.get(j), f).getSnd();
                        // System.out.println(sum + ", two");
                    } else if (i == j) {
                        sum += this.solve(h.get(i), f).getSnd();
                        // System.out.println(sum + ", three");
                    }
                }

            }
        }
        sum += s*(s - 1)/2;
        // System.out.println(sum + ", single");
        return sum; 
    }

    public Pair<?, Integer> solve(List<String> ans1, List<String> ans2, 
    BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> f) {
        if (ans1.size() == 1) {
            String s = ans1.get(0);
            // the following won't work as miniSolve may give maxvalue if s is non-separating!
            // return new Pair<>(s, super.miniSolve(s, ans2));
            
            // if (super.miniSolve(s, ans2) == Integer.MAX_VALUE) {
            //     int x = super.solve(ans2).getSnd();
            //     return new Pair<>(s, x + ans2.size());
            // }
            // return new Pair<>(s, super.miniSolve(s, ans2));

            HashMap<Integer, List<String>> h = this.check(s, ans2);
            int sum = 0;
            for (int i : h.keySet()) {
                sum += super.solve(h.get(i), 1).getSnd();
            }
            return new Pair<>(s, sum + ans2.size()); 
        } 

        if (ans2.size() == 1) {
            String s = ans2.get(0);
            HashMap<Integer, List<String>> h = this.check(s, ans1);
            int sum = 0;
            for (int i : h.keySet()) {
                sum += super.solve(h.get(i), 1).getSnd();
            }
            return new Pair<>(s, sum + ans1.size()); 
        }

        List<String> succ = this.succ(ans1, ans2, f);
        return succ.stream().map(w -> new Pair<>(w, this.miniSolve(w, ans1, ans2, f))).parallel()
        .reduce(new Pair<>(null, 6 * ans1.size() * ans2.size()), (y, z) -> (y.getSnd() < z.getSnd()) ? y : z);
    }

    public int miniSolve(String w, List<String> ans1, List<String> ans2,
    BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> f) {
        HashMap<Integer, List<String>> h1 = this.check(w, ans1);
        HashMap<Integer, List<String>> h2 = this.check(w, ans2);
        int sum = 0;
        int c = (int) Math.pow(3, this.l) - 1;
        // note that it is impossible for c to be in both h1 and h2
        // since that implies w \in ans1 and w \in ans2, but ans1 and ans2 are disjoint
        for (int i : h1.keySet()) {
            if (i == c) {
                for (int j : h2.keySet()) {
                    sum += super.solve(h2.get(j),1).getSnd();
                }
                
            } else {
                for (int j : h2.keySet()) {
                    if (j == c) {
                        sum += super.solve(h1.get(i), 1).getSnd();
                    } else {
                        sum += this.solve(h1.get(i), h2.get(j), f).getSnd();
                    }
                }
            }
        }
        int i = sum + ans1.size()*ans2.size();
        // System.out.println(i + "double");
        return i;
    }
    
}
