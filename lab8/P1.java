import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class P1 {
    static int maxXs = 0;
    static int area = 0;
    static Reader input = new InputStreamReader(System.in);
    static BufferedReader reader = new BufferedReader(input);
    static int xCount = 0;
    static int counter = 0;
    static ArrayList<Integer> xs = new ArrayList<>();

    public static void main(String... ignored) {
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                for (int i = 0; i < line.length(); i ++) {
                    if (line.charAt(i) == 'X') {
                        xCount += 1;
                    }
//
                    counter += 1;
                }
                xs.add(xCount);

            }
            returnArea();

        } catch (IOException e) {
            System.out.print("You goofed");

        }

    }

    /** returns the area. */
    public static int returnArea () {
        maxXs = Collections.max(xs);
        for (int j = 0; j < xs.size(); j++) {
            area += (maxXs - xs.get(j));
        }
        return area;
    }


}
