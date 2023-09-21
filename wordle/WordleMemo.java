package wordle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// The original version seems to be correct and faster for some reason
public class WordleMemo extends Wordle {
    public WordleMemo(List<String> allowed, List<String> ans, int l) {
        super(allowed, ans, l);
    }

    // To make it truly memoized, the hashmaps check(w, l) should NOT be computed
    // twice, again in the solve method.
    // But most of the list isn't in succ anyways, so how much does it really
    // matter?

    // Almost to no extent lmao.

    // No correctness issue here
    // Alt only
    // public List<Pair<String, HashMap<Integer, List<String>>>>
    // succMemo(List<String> l) {

    // This is slow because of the 'satellite data' so removed

    @Override
    public Pair<NestedMap<Integer, String, List<String>>, Integer> solve(List<String> ans, int x) {
        int y = ans.size();
        if (y < 3) {
            NestedMap<Integer, String, List<String>> nm = new NestedMap<>(ans.get(0), List.of(ans.get(0)));
            nm.put(c, null);
            if (y == 1) {
                return new Pair<>(nm, 1);
            }
            NestedMap<Integer, String, List<String>> nm2 = new NestedMap<>(ans.get(1), ans);
            nm2.put(this.compare.get(ans.get(1)).get(ans.get(0)),
                    nm);
            nm2.put(c, null);
            return new Pair<>(nm2, 3);
        }

        if (y < 20) {
            int max = 0;
            int n = 0;
            HashMap<Integer, List<String>> g = new HashMap<>();
            for (int i = 0; i < y; i++) {
                HashMap<Integer, List<String>> h = this.check(ans.get(i), ans);
                if (h.size() == y) {
                    NestedMap<Integer, String, List<String>> nm = new NestedMap<>(ans.get(i), null);
                    for (int j : h.keySet()) {
                        nm.put(j, this.solve(h.get(j), x).getFst());
                    }
                    nm.replace(c, null);
                    return new Pair<>(nm, 2 * y - 1);
                }
                if (h.size() > max) {
                    max = h.size();
                    n = i;
                    g = h;
                }
            }
            if (max == y - 1) {
                NestedMap<Integer, String, List<String>> nm = new NestedMap<>(ans.get(n), null);
                for (int j : g.keySet()) {
                    nm.put(j, this.solve(g.get(j), x).getFst());
                }
                nm.replace(c, null);
                return new Pair<>(nm, 2 * y);
            }
        }

        // Original
        List<String> l = this.succ(ans, x);
        Pair<NestedMap<Integer, String, List<String>>, Integer> p = l.stream().parallel()
                .map(z -> this.miniSolveMemo(z, ans, x))
                .reduce(new Pair<NestedMap<Integer, String, List<String>>, Integer>(null, Integer.MAX_VALUE),
                        (z, aa) -> (z.getSnd() < aa.getSnd()) ? z : aa);
        return p;
    }

    public Pair<NestedMap<Integer, String, List<String>>, Integer> miniSolveMemo(String s, List<String> l, int x) {
        // public Pair<Tree<String, Integer>, Integer> miniSolveMemo(Pair<String,
        // HashMap<Integer, List<String>>> p
        // , List<String> l) {

        int sum = 0;

        HashMap<Integer, List<String>> h = this.check(s, l); // for original

        // String s = p.getFst();
        // HashMap<Integer, List<String>> h = p.getSnd(); // alt

        NestedMap<Integer, String, List<String>> nm = new NestedMap<>(s, null);
        nm.setW(l);

        if (h.size() == 1) {
            return new Pair<>(null, Integer.MAX_VALUE);
        }
        for (int j : h.keySet()) {
            Pair<NestedMap<Integer, String, List<String>>, Integer> p1 = this.solve(h.get(j), x);
            sum += p1.getSnd();
            nm.put(j, p1.getFst());
            // initialise the lists for everything
            nm.get(j).setW(h.get(j));
        }

        sum += l.size();

        if (h.containsKey(c)) {
            sum -= 1;
            nm.replace(c, null);
        }
        return new Pair<>(nm, sum);
    }

    public static List<List<String>> print(NestedMap<Integer, String, List<String>> nm) {
        if (nm == null) {
            List<List<String>> l = new ArrayList<>();
            l.add(new ArrayList<String>());
            return l;
        }
        List<List<String>> l = new ArrayList<>();
        for (Integer i : nm.keySet()) {
            l.addAll(print(nm.get(i)));
        }
        for (List<String> li : l) {
            li.add(0, nm.getV());
        }
        return l;
    }

}
