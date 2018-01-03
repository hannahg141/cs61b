import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
public class P3 {

    static Reader input = new InputStreamReader(System.in);
    static BufferedReader reader = new BufferedReader(input);

    public static void main(String... ignored) {
        try {
            String input = reader.readLine();


            System.out.print("The smallest good numeral of length " + input + "is " + S);

        } catch (IOException e) {
            System.out.print("You goofed");

        }


    }


}
