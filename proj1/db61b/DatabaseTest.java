package db61b;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DatabaseTest {

    public static void main(String[] args) {

        System.exit(ucb.junit.textui.runClasses(DatabaseTest.class));
    }



    @Test
    public void get() {
        Database database = new Database();
        Table tableTest = new
                Table(new
                String[] {"SID", "Lastname", "Firstname",
                    "SemEnter", "YearEnter", "Major"});
        String[] row1 =
                new String[]{"101", "Knowles", "Jason", "F", "2003", "EECS"};
        tableTest.add(row1);
        database.put("HANNAH", tableTest);
        assertEquals(tableTest, database.get("HANNAH"));


    }



}

