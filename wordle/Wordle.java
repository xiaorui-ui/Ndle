package wordle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


// import javafx.util.Pair;




public class Wordle {

    protected final List<String> allowed;
    protected final List<String> ans;
    protected final int l;
    // Used to represent when the Wordle is solved(everything green)
    protected final int c;

    // 2 choices:
    //     1. HashMap<String, Integer>
    //     2. HashMap<String, HashMap<String, Integer>>
    // 2 is faster
    // Need to understand hashing better to understand why, probably.
    protected final HashMap<String, HashMap<String, Integer>> compare;

    public Wordle(List<String> allowed, List<String> ans, int l) {
        this.allowed = allowed;
        this.ans = ans;
        this.l = l;
        this.c = (int) Math.pow(3, l) - 1;
        this.compare = Wordle.hash(allowed, ans, l);
    }

    public static HashMap<String, HashMap<String, Integer>> hash(List<String> allowed, List<String> ans, int l) {
        HashMap<String, HashMap<String, Integer>> h = new HashMap<>();
        for (int i = 0 ; i < allowed.size(); i++) {
            String s1 = allowed.get(i);
            for (int j = 0 ; j < ans.size(); j++) {
                String s2 = ans.get(j);
                if (j == 0) {
                    h.put(s1, new HashMap<String, Integer>());
                }
                h.get(s1).put(s2, Wordle.compare(s1, s2, l));
            }
        }
        return h;
    }

    // Slower to get than hash
    public static HashMap<String, Integer> hash2(List<String> allowed, List<String> ans, int l) {
        HashMap<String, Integer> h = new HashMap<>();
        for (int i = 0 ; i < allowed.size(); i++) {
        String s1 = allowed.get(i);
            for (int j = 0 ; j < ans.size(); j++) {
                String s2 = ans.get(j);
                h.put(s1 + s2, Wordle.compare(s1, s2, 5));
            }
        }
        return h;
    }

    // 2 represents green, 1 represents yellow, 0 represents black/grey
    public static int compare(String a, String b, int l) {
        assert(a.length() == l && b.length() == l);
        int[] arr1 = new int[l];
        int[] arr2 = new int[l];

        for (int i = 0; i < l; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                arr1[i] = 2;
                arr2[i] = 2;
            }
        }

        // Actually we also need i != j
        // Else we can just continue
        // how can this be done most efficiently? 
        // Maybe test it out again using timer?

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    if (arr1[i] == 0 && arr2[j] == 0) {
                        arr1[i] = 1;
                        arr2[j] = 1;
                        break;                  
                    }
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < l; i++) {
            ans += (int) arr1[i] * Math.pow(3, i);
        }
        return ans;
    }


    // We assume l is a subset of ans
    // Else we can just add on to compare but we haven't implemented that yet 
    public HashMap<Integer, List<String>> check(String w, List<String> l) {
        HashMap<Integer, List<String>> h = new HashMap<>();
        for (int j = 0; j < l.size(); j++) {
            // The key for faster computation is getting the value instead of computing it again
            // Or ... is it?
            String w2 = l.get(j);
            int key = this.compare.get(w).get(w2);
            // Integer key = Wordle.compare(word, l.get(j), this.l);
            if (!h.containsKey(key)) {
                h.put(key, new ArrayList<String>());
            }
            h.get(key).add(w2);
        } 
        return h;
    }

    // The sibling of the above function, but HashSet instead
    // So only contains the set of distinct comparisons
    public HashSet<Integer> checkSet(String w, List<String> l) {
        HashSet<Integer> h = new HashSet<>();
        for (int j = 0; j < l.size(); j++) {
            int key = this.compare.get(w).get(l.get(j));
            // Integer key = this.compare.get(word).get(l.get(j));
            if (!h.contains(key)) {
                h.add(key);
            }
        } 
        return h;
    }


    // pick the best k successors, based on number of groups(the larger the better)
    // or try smallest largest group size, this would probably be better
    // uses previous function so again assume l subset of ans
    public List<String> succ(List<String> l, int t) {
        int x = this.allowed.size();
        int y = ans.size();

        List<Pair<String, Integer>> groups = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            HashSet<Integer> h = this.checkSet(s, l);
            if (h.size() == y) {
                return List.of(s);
            }
            groups.add(new Pair<>(s, h.size()));            
        }

        Collections.sort(groups, (z, zz) -> {
            return zz.getSnd() - z.getSnd();
        });

        // 5 is a magic number, change if needed
        // return 6 (0-indexed) + ties
        int threshold = groups.get(Math.min(t, groups.size() - 1)).getSnd();
        List<String> li = new ArrayList<>();
        int i = 0;
        while (i < x && groups.get(i).getSnd() >= threshold) {
            li.add(groups.get(i).getFst());
            i++;
        }

        return li;
    }

    

    // We assume answers subseteq allowed, of course
    // Otherwise we may never reach the right answer
    // We can actually return a double of average instead of total

    // assume allowed subset of this.allowed, ans subset of this.ans
    public Pair<?, Integer> solve(List<String> ans, int t) {
        int y = ans.size();

        if (y < 3) {
            return new Pair<>(ans.get(0), 2 * y - 1);
        } 

        if (y < 20) {
            int max = 0;
            String s = "";
            for (int i = 0; i < y; i++) {
                // So alas, the mistake is here LOL
                // previously it was checkSet(i, ans), which is clearly wrong as we are doing ans.get(i) instead of this.allowed.get(i)
                // Even though the answer is only ~100 more, there seems to be a lot more recursion
                HashSet<Integer> h = this.checkSet(ans.get(i), ans);
                if (h.size() == y) {
                    return new Pair<>(ans.get(i), 2 * y - 1);
                }
                if (h.size() > max) {
                    max = h.size();
                    s = ans.get(i);
                }               
            }
            if (max == y - 1) {
                return new Pair<>(s, 2 * y);
            }
        }

        List<String> l = this.succ(ans, t);

        return l.stream().parallel().map(x -> new Pair<>(x, this.miniSolve(x, ans, t)))
        .reduce(new Pair<String, Integer>("", 6 * y), (z, aa) -> (z.getSnd() < aa.getSnd()) ? z : aa);
    }

    // ans subset of this.ans
    public int miniSolve(String word, List<String> ans, int t) {
        HashMap<Integer, List<String>> h = this.check(word, ans);
        // This bit is important! Else infinite loop if you keep trying the same word!
        if (h.size() == 1) {
            return Integer.MAX_VALUE;
        } else if (h.size() == ans.size()) {
            return 2 * ans.size();
        }

        int sum = 0;
        for (Integer j: h.keySet()) {
            //System.out.println(j + ", " + this.solve(h.get(j)).getSnd());
            sum += this.solve(h.get(j), t).getSnd();
        }
        sum += ans.size();

        // For correctness, you need to consider the case when your guess is instantly correct. 
        if (h.containsKey(c)) {
            sum -= 1;
        }
        return sum;
    }

    
    // for de-bugging
    public void print(List<String> ans) {
        int x = allowed.size();

        List<Pair<String, Integer>> groups = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = allowed.get(i);
            HashSet<Integer> h = this.checkSet(s, ans);
            groups.add(new Pair<>(allowed.get(i), h.size()));            
        }

        
        Collections.sort(groups, (z, zz) -> {
            return zz.getSnd() - z.getSnd();
        });

        for (int i = 0; i < Math.min(x, 100); i++) {
            System.out.println(groups.get(i));
        }
        
    }
    
}
