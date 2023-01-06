package test;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Constants {
    public static final List<String> fiveAllowed = Constants.init("allowed");

    public static final List<String> fiveAns = Constants.init("answer");

    public static final List<String> nyt = Constants.init("nyt");

    public static final List<String> nytAllowed = Constants.init("nytAllowed");

    public static List<String> init(String str) {
        List<String> l = new ArrayList<>(); 
        //fiveAllowed
        try {
            Scanner s = new Scanner(new File(str + ".txt"));
            while (s.hasNextLine()){
                l.add(s.nextLine());
            }
            // System.out.println(allowed);
            s.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //System.out.println(l.size());

        return l;

    }

    public static final List<String> l1 = List.of("aaaaa", "baaaa", "caaaa", "daaaa","eaaaa", "faaaa", "gaaaa", "haaaa", 
        "iaaaa", "jaaaa");

    public static final List<String> l2 = List.of("aaaaa", "baaaa", "caaaa", "daaaa","eaaaa", "faaaa", "gaaaa", "haaaa", 
        "iaaaa");

    public static final List<String> l3 = List.of("moral", "coral", "royal",
    "rival", "flora", "mural", "lycra", "rural", "viral", "aural");

}
