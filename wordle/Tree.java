package wordle;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


// a nested Entry - HashMap - Entry
// In our case, R = String, S = Integer
// In the bottom-most layer, Tree = null
public class Tree<R, S> {
    // private final Entry<R, HashMap<S, Tree>> e;
    private final Entry<R, HashMap<S, Tree<R, S>>> e;

    public Tree(R r) {
        this.e = new SimpleImmutableEntry<>(r, new HashMap<>());
    }

    public void put(S s, Tree<R, S> tree) {
        this.e.getValue().put(s, tree);
    }

    public Tree<R, S> get(S s) {
        return this.e.getValue().get(s);        
    }

    public void replace(S s, Tree<R, S> t) {
        this.e.getValue().replace(s, t);
    }

    public R getKey() {
        return this.e.getKey();
    }

    public Set<S> keySet() {
        return this.e.getValue().keySet();
    }

    public int deepest() {
        int max = 1;
        for (S i: this.keySet()) {
            if (this.get(i) != null) {
                max = Math.max(max, 1 + this.get(i).deepest());
            }
        }
        return max;        
    }
    
    @Override
    public String toString() {
        return this.e.toString();
    }
    
}
