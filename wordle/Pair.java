package wordle;


public class Pair<S, T> {
    private S s;
    private T t;

    public Pair(S s, T t) {
        this.s = s;
        this.t = t;
    }

    public S getFst() {
        return this.s;
    }

    public void setFst(S s) {
        this.s = s;
    }

    public T getSnd() {
        return this.t;
    }

    public void setSnd(T t) {
        this.t = t;
    } 

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair<?, ?>)) {
            return false;
        }
        Pair<? ,?> p = (Pair<?, ?>) o;
        // We ignore null case for now
        return this.s.equals(p.s) && this.t.equals(p.t);       
    }
    
    @Override
    public String toString() {
        return this.s.toString() + "," + this.t.toString();
               
    } 
    
}
