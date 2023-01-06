package test;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class Functions {
    // Toy measure functions that aren't useful yet, Function<HashMap<?, ? extends List<?>>, Integer>
    // Functions return a pair, first value is the actual value, second is the 'ideal value'

    // For all functions, the SMALLER the better

    // best = n

    // number of buckets
    // best = n   
    public static final BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> size = 
    (x, y) -> {
        return 2*x.size() - y.size();
    };

    // size of largest bucket
    // best = n
    public static final BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> max = 
    (x, y) -> {
        int i = 0;
        for (Object o: y.keySet()) {
            i = Math.max(i, y.get(o).size());
        }
        return x.size()*i;
    };
    
    // min quadratic sum
    // best = n
    public static final BiFunction<List<?>, HashMap<?, ? extends List<?>>, Integer> qSum = 
    (x, y) -> {
        int i = 0;
        for (Object o: y.keySet()) {
            i += y.get(o).size()*y.get(o).size();
        }
        return i;        
    };

    // max entropy, base doesn't matter though in Java it's e
    // ideal value = 0
    // we ignore this for now
    public static final BiFunction<List<?>, HashMap<?, ? extends List<?>>, Double> entropy = 
    (x, y) -> {
        return (double) 0;
    };
    
}
