import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import test.*;

public class Ndle {
    public static void main(String[] args) {
        System.out.println("Processing power: " + Runtime.getRuntime().availableProcessors());
        new WordleRun().run();
        
        // Path out = Paths.get("output.txt");
        // List<String> arrayList = new ArrayList<> ( Arrays.asList ( "a" , "b" , "c" ) );
        // try {
        //     Files.write(out,arrayList,Charset.defaultCharset());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        

        // new DordleRun().run();
        
    }
}
