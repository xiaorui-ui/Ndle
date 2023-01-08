package wordle;

import java.util.HashMap;
import java.util.Set;

// Omg I could have just used this in place of Tree...
// K = Integer, V = String
// Can also use more 'satellite data' by adding more attributes
public class NestedMap<K, V, W> {

    private final HashMap<K, NestedMap<K, V, W>> child;
    private V v;
    private W w;

    // Overloaded constructor
    public NestedMap() {
        this.child = new HashMap<>();
        this.v = null;
        this.w = null;
    }

    public NestedMap(V v, W w) {
        this.child = new HashMap<>();
        this.v = v;
        this.w = w;
    }




    public boolean containsKey(K k) {
        return this.child.containsKey(k);
    }

    public Set<K> keySet() {
        return this.child.keySet();
    }
    
    public NestedMap<K, V, W> get(K k) {
        return this.child.get(k);
    }

    // Overloaded put method
    public void put(K k) {
        this.child.put(k, new NestedMap<>());
    }
    
    public void put(K k, NestedMap<K, V, W> nm) {
        this.child.put(k, nm);
    }



    public void replace(K s, NestedMap<K, V, W> t) {
        this.child.replace(s, t);
    }

    public V getV() {
        return this.v;
    }

    public W getW() {
        return this.w;
    }

    public void setV(V v) {
        this.v = v;
    }

    public void setW(W w) {
        this.w = w;
    }

    public int deepest() {
        int max = 1;
        for (K i: this.keySet()) {
            if (this.get(i) != null) {
                max = Math.max(max, 1 + this.get(i).deepest());
            }
        }
        return max;        
    }

    public int size() {
        return this.child.size();
    }

    public int nestedSize() {
        int s = 0;
        for (K k: this.keySet()) {
            s += this.get(k).size();
        }
        return s;
    }

    @Override
    public String toString() {
        if (this.w == null && this.v == null) {
            return "null,null:" + this.child.toString();
        } 
        if (this.w == null) {
            return this.v.toString() + ",null:" + this.child.toString();
        } 
        if (this.v == null) {
            return "null," + this.w.toString() + ":" + this.child.toString();
        }
        return this.v.toString() + "," + this.w.toString() + ":" + this.child.toString();
    }
}
