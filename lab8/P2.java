import apple.laf.JRSUIUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class P2 {

    static Reader input = new InputStreamReader(System.in);
    static BufferedReader reader = new BufferedReader(input);

    public static void main(String... ignored) {

        try {
            String input = reader.readLine();
            //each input is one or more cases in tree format
                //each case has 2 nonempty strings PRE and IN
                    //case is significant
            makeTree()




        } catch (IOException e) {
            System.out.print("You goofed");
        }

    }

    public static JRSUIUtils.Tree makeTree() {
        JRSUIUtils.Tree tree = new JRSUIUtils.Tree();
    }


}
