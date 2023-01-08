package test;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.HashMap;
import java.util.List;


import wordle.*;

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

    // helper for entropy with nested map
    public static BiFunction<BiFunction, NestedMap<Integer, String, List<String>>, Double> helperE = 
    (f, nm) -> {
        // corresponding to bottom most layer
        if (nm == null) {
            return (double) 0;
        }
        if (nm.size() == 0) {
            int s = nm.getW().size();
            return Math.log(s)*s;            
        }
        double d = 0;
        for (Integer i: nm.keySet()) {

    // CANNOT REFERENCE A FIELD BEFORE IT'S DEFINED!!!!! That's why the helper function is needed

            d += (double) f.apply(f, nm.get(i));
        }
        return d;
    };

    //entropy with nested map
    public static Function<NestedMap<Integer, String, List<String>>, Double> entropyNested = 
    nm -> {
        return helperE.apply(helperE, nm);        
    };

    // helper for size with nested map
    public static BiFunction<BiFunction, NestedMap<Integer, String, List<String>>, Integer> helperS = 
    (f, nm) -> {
        // corresponding to bottom most layer
        if (nm == null) {
            return -1;
        }
        if (nm.size() == 0) {
            return -1;            
        }
        int z = 0;
        for (Integer i: nm.keySet()) {
            z += (int) f.apply(f, nm.get(i));
        }
        return z;
    };

    // size with nested map
    public static Function<NestedMap<Integer, String, List<String>>, Integer> sizeNested = 
    nm -> {
        return helperS.apply(helperS, nm);        
    };
    
}
