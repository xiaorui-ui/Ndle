package wordle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;




// The original version seems to be correct and faster for some reason
public class WordleMemo extends Wordle {
    public WordleMemo(List<String> allowed, List<String> ans, int l) {
        super(allowed, ans, l);
    }

    // To make it truly memoized, the hashmaps check(w, l) should NOT be computed twice, again in the solve method.
    // But most of the list isn't in succ anyways, so how much does it really matter?

    // Almost to no extent lmao.

    // No correctness issue here
    // Alt only
    // public List<Pair<String, HashMap<Integer, List<String>>>> succMemo(List<String> l) {

    // This is slow because of the 'satellite data' so removed

    @Override
    public Pair<Tree<String, Long>, Integer> solve(List<String> ans, int x) {
        int y = ans.size();
        if (y < 3) {
            if (y == 2) {
                Tree<String, Long> t = this.solve(ans.subList(0, 1), x).getFst();
                t.put((long) this.compare.get(ans.get(0)).get(ans.get(1)),
                 this.solve(ans.subList(1, 2), x).getFst());
                return new Pair<>(t, 3);
            }
            Tree<String, Long> t = new Tree<>(ans.get(0));
            t.put((long) Math.pow(3, this.l) - 1, null);
            return new Pair<>(t, 1);
            
        }
        
        if (y < 20) {
            int max = 0;
            int n = 0;
            HashMap<Integer, List<String>> g = new HashMap<>();            
            for (int i = 0; i < y; i++) {
                HashMap<Integer, List<String>> h = this.check(ans.get(i), ans);
                if (h.size() == y) {
                    Tree<String, Long> t = new Tree<>(ans.get(i));
                    for (int j: h.keySet()) {
                        t.put((long) j, this.solve(h.get(j), x).getFst());                       
                    }
                    t.replace((long) c, null);                    
                    return new Pair<>(t, 2 * y - 1);                  
                }
                if (h.size() > max) {
                    max = h.size();
                    n = i;
                    g = h;
                }
            }
            if (max == y - 1) {
                Tree<String, Long> t = new Tree<>(ans.get(n));
                for (int j: g.keySet()) {
                    t.put((long) j, this.solve(g.get(j), x).getFst());                        
                }
                t.replace((long) c, null);                    
                return new Pair<>(t, 2 * y);                
            }
        }


        // Original
        List<String> l = this.succ(ans, x);
        Pair<Tree<String, Long>, Integer> p =  l.stream().parallel().map(z -> this.miniSolveMemo(z, ans, x))
        .reduce(new Pair<Tree<String, Long>, Integer>
        (null, Integer.MAX_VALUE), (z, aa) -> (z.getSnd() < aa.getSnd()) ? z : aa);
        return p;

        // Alt
        // List<Pair<String, HashMap<Integer, List<String>>>> l = this.succMemo(ans);
        // Pair<Tree<String, Integer>, Integer> p = l.stream().parallel().map(x -> this.miniSolveMemo(x, ans))
        // .reduce(new Pair<Tree<String, Integer>, Integer>
        // (null, Integer.MAX_VALUE), (z, aa) -> (z.getSnd() < aa.getSnd()) ? z : aa);
        // // System.out.println(p);
        // return p;
    }
    
    public Pair<Tree<String, Long>, Integer> miniSolveMemo(String s, List<String> l, int x) {
    // public Pair<Tree<String, Integer>, Integer> miniSolveMemo(Pair<String, HashMap<Integer, List<String>>> p
    // , List<String> l) {
    
        int sum = 0;

        HashMap<Integer, List<String>> h = this.check(s, l); // for original

        // String s = p.getFst();
        // HashMap<Integer, List<String>> h = p.getSnd(); // alt

        Tree<String, Long> t = new Tree<>(s);

        if (h.size() == 1) {
            return new Pair<>(null, Integer.MAX_VALUE);
        } 
        for (int j: h.keySet()) {
            Pair<Tree<String, Long>, Integer> p1 = this.solve(h.get(j), x);
            sum += p1.getSnd();
            t.put((long) j, p1.getFst());
        }

        sum += l.size();
 
        if (h.containsKey(c)) {
            sum -= 1;
            t.replace((long) c, null);
        }        
        return new Pair<>(t, sum);        
    }

    
    // for de-bugging
    @Override
    public void print(List<String> ans) {
        int x = allowed.size();

        List<Pair<String, HashMap<Integer, List<String>>>> groups = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = allowed.get(i);
            HashMap<Integer, List<String>> h = this.check(s, ans);
            groups.add(new Pair<>(s, h));            
        }

        
        Collections.sort(groups, (z, zz) -> {
            return zz.getSnd().size() - z.getSnd().size();
        });

        for (int i = 0; i < Math.min(x, 100); i++) {
            System.out.println(groups.get(i).getFst() + "," + groups.get(i).getSnd().size());
        }
        
    }

    // consider including this as a public method in tree instead
    // null pointer exception nvm LOL
    public static List<List<String>> print(Tree<String, Long> t) {
        if (t == null) {
            List<List<String>> l = new ArrayList<>();
            l.add(new ArrayList<String>());
            return l;
        }
        List<List<String>> l = new ArrayList<>();
        for (Long i: t.keySet()) {
            l.addAll(print(t.get(i)));
        }
        for (List<String> li: l) {
            li.add(0, t.getKey());
        }
        return l;
    }

}
