import java.util.HashSet;
import java.util.Set;

/**
 * @author hannahgrossman
 */

public abstract class SetDemo implements Set{

    public static void main(String[] args) {
        Set<String> mySet;
        mySet = new HashSet<String>();
        mySet.add("papa");
        mySet.add("bear");
        mySet.add("mama");
        mySet.add("bear");
        mySet.add("baby");
        mySet.add("bear");
        System.out.print(mySet);


    }

    public String toString() {
        return "I'm Confused";
    }

}


