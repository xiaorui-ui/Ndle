package dordle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import wordle.*;

public class Dordle extends Wordle {

    private int count;

    public Dordle(List<String> allowed, List<String> ans, int l) {
        super(allowed, ans, l);
        this.count = 0;
    }

    // Return largest only + ties now
    // Also notice if all the partitions are <= size 2 we can return instantly
    public List<String> succ(List<String> l) {

        this.count += 1;

        int x = this.allowed.size();
        int y = l.size();
        int max = 0;
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            HashSet<Integer> h = this.checkSet(s, l);
            if (h.size() >= max) {
                if (h.size() == y) {
                    return List.of(s);
                }
                if (h.size() != max) {
                    max = h.size();
                    ans = new ArrayList<>();
                }
                ans.add(s);
            }
        }
        return ans;
    }

    // When the sets are disjoint
    // It's highly improbable that a trial word can completely separate 2 sets so we
    // ignore that possibility here
    public List<String> succ(List<String> l1, List<String> l2) {

        this.count += 1;

        int x = this.allowed.size();
        int max = 0;
        List<String> ans = new ArrayList<>();
        int y = l1.size();
        int z = l2.size();
        for (int i = 0; i < x; i++) {
            String s = this.allowed.get(i);
            HashSet<Integer> h1 = this.checkSet(s, l1);
            HashSet<Integer> h2 = this.checkSet(s, l2);
            int prod = h1.size() * h2.size();
            if (prod >= max) {
                // can abstract out this part also
                if (prod == y * z) {
                    return List.of(s);
                }
                if (prod != max) {
                    max = prod;
                    ans = new ArrayList<>();
                }
                ans.add(s);
            }
        }
        return ans;
    }

    public Pair<?, Integer> solve(List<String> l) {
        int x = l.size();
        if (x <= 2) {
            return new Pair<>(l.get(0), 2 * x - 2);
        }
        // happy case
        if (x < 20) {
            int min = Integer.MAX_VALUE;
            String ans = "";
            for (int k = 0; k < x; k++) {
                String w = l.get(k);
                HashMap<Integer, List<String>> h = this.check(w, l);
                int y = this.solve(h);
                if (y < min) {
                    min = y;
                    ans = w;
                }
            }
            if (min <= 2 * x * (x - 1) / 2) {
                return new Pair<>(ans, min + x * (x - 1) / 2);
            }
        }
        List<String> succ = this.succ(l);
        Pair<?, Integer> p = succ.stream().map(w -> new Pair<>(w, this.miniTwoSolve(w, l)))
                .reduce(new Pair<>(null, 4 * l.size() * l.size()), (y, z) -> (y.getSnd() < z.getSnd()) ? y : z);
        p.setSnd(p.getSnd() + x * (x - 1) / 2);
        System.out.println(this.count);
        return p;
    }

    public int miniTwoSolve(String word, List<String> ans) {
        int s = ans.size();
        HashMap<Integer, List<String>> h = this.check(word, ans);
        // This case shouldn't occur as word is either an acceptable answer, or a most
        // splitting word

        if (h.size() == 1) {
            return Integer.MAX_VALUE;
        }

        if (h.size() == s) {
            return 2 * s * (s - 1) / 2;
        }
        return this.solve(h);
    }

    public int solve(HashMap<Integer, List<String>> h) {
        int sum = 0;
        for (int i : h.keySet()) {
            if (i == c) {
                // The minisolve will reference !this solve method instead!
                // sum += super.miniSolve(word, ans) - ans.size();
                for (int j : h.keySet()) {
                    sum += super.solve(h.get(j), 1).getSnd();
                }
                // System.out.println(sum);

                // if j == c also LOL
                sum -= 1;

            } else {
                for (int j : h.keySet()) {
                    if (j < i) {
                        sum += this.solve(h.get(i), h.get(j)).getSnd();
                        // System.out.println(sum + ", two");
                    } else if (i == j) {
                        sum += this.solve(h.get(i)).getSnd();
                        // System.out.println(sum + ", three");
                    }
                }

            }
        }
        // System.out.println(sum + ", single");
        return sum;
    }

    public Pair<?, Integer> solve(List<String> ans1, List<String> ans2) {
        int x = ans1.size();
        int xx = ans2.size();
        if (x == 1) {
            String s = ans1.get(0);
            // the following won't work as miniSolve may give maxvalue if s is
            // non-separating!
            // return new Pair<>(s, super.miniSolve(s, ans2));

            // int z = super.miniSolve(s, ans2);
            // if (z == Integer.MAX_VALUE) {
            // Pair<?, Integer> p = super.solve(ans2);
            // p.setFst(s); // This line doesn't work!!!
            // p.setSnd(p.getSnd() + xx);
            // return p;
            // }
            // return new Pair<>(s, z);

            HashMap<Integer, List<String>> h = this.check(s, ans2);
            int sum = 0;
            for (int i : h.keySet()) {
                sum += super.solve(h.get(i), 1).getSnd();
            }
            return new Pair<>(s, sum + xx);
        }

        if (xx == 1) {
            String s = ans2.get(0);
            HashMap<Integer, List<String>> h = this.check(s, ans1);
            int sum = 0;
            for (int i : h.keySet()) {
                sum += super.solve(h.get(i), 1).getSnd();
            }
            return new Pair<>(s, sum + x);
        }

        // happy case, once again
        if (x < 15 && xx < 15) {
            String w = "";
            int min = Integer.MAX_VALUE;
            int tot = x + xx;
            for (int i = 0; i < tot; i++) {
                String t;
                if (i < x) {
                    t = ans1.get(i);
                } else {
                    t = ans2.get(i - x);
                }

                HashMap<Integer, List<String>> h1 = super.check(t, ans1);
                HashMap<Integer, List<String>> h2 = super.check(t, ans2);
                int y = this.solve(h1, h2);
                if (y < min) {
                    w = t;
                    min = y;
                }
            }
            if (min <= 2 * x * xx) {
                return new Pair<>(w, min + x * xx);
            }
        }

        List<String> succ = this.succ(ans1, ans2);
        Pair<?, Integer> p = succ.stream().map(w -> new Pair<>(w, this.miniSolve(w, ans1, ans2))).parallel()
                .reduce(new Pair<>(null, 6 * ans1.size() * ans2.size()), (y, z) -> (y.getSnd() < z.getSnd()) ? y : z);
        p.setSnd(p.getSnd() + ans1.size() * ans2.size());
        return p;
    }

    public int miniSolve(String w, List<String> ans1, List<String> ans2) {
        HashMap<Integer, List<String>> h1 = this.check(w, ans1);
        HashMap<Integer, List<String>> h2 = this.check(w, ans2);
        if (h1.size() == 1 && h2.size() == 1) {
            return Integer.MAX_VALUE;
        }
        if (h1.size() == ans1.size() && h2.size() == ans2.size()) {
            return 2 * ans1.size() * ans2.size();
        }
        return this.solve(h1, h2);
    }

    public int solve(HashMap<Integer, List<String>> h1, HashMap<Integer, List<String>> h2) {
        int sum = 0;
        for (int i : h1.keySet()) {
            if (i == c) {
                for (int j : h2.keySet()) {
                    sum += super.solve(h2.get(j), 1).getSnd();
                }

            } else {
                for (int j : h2.keySet()) {
                    if (j == c) {
                        sum += super.solve(h1.get(i), 1).getSnd();
                    } else {
                        sum += this.solve(h1.get(i), h2.get(j)).getSnd();
                    }
                }
            }
        }
        // System.out.println(i + "double");
        return sum;

    }

}
