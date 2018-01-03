
package db61b;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TableTest {

    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(TableTest.class));
    }

    Table myTable;

    private String[] columnTitles = new
            String[]{"SID", "Lastname", "Firstname",
                     "SemEnter", "YearEnter", "Major"};
    private String[] row1 =
            new String[]{"101", "Knowles", "Jason", "F", "2003", "EECS"};
    private String[] row2 =
            new String[]{"102", "Chan", "Valerie", "S", "2003", "Math"};
    private String[] row3 =
            new String[]{"103", "Xavier", "Jonathan", "S", "2004", "LSUnd"};
    private String[] row4 =
            new String[]{"104", "Armstrong", "Thomas", "F", "2003", "EECS"};
    private String[] row5 =
            new String[]{"105", "Brown", "Shana", "S", "2004", "EECS"};
    private String[] row6 =
            new String[]{"106", "Chan", "Yangfan", "F", "2003", "LSUnd"};


    @Before
    public void setUp() {
        myTable = new Table(columnTitles);
        myTable.add(row1);
        myTable.add(row2);
        myTable.add(row3);
        myTable.add(row4);
        myTable.add(row5);
        myTable.add(row6);


    }

    @Test
    public void testColumns() {
        assertEquals(6, myTable.columns());
    }


    @Test
    public void getTitle() {
        assertEquals("Lastname", myTable.getTitle(1));

    }

    @Test
    public void testFindColumn() {
        assertEquals(1, myTable.findColumn("Lastname"));
    }

    @Test
    public void testGet() {
        assertEquals("Knowles", myTable.get(0, 1));

    }

    @Test
    public void testSize() {
        assertEquals(6, myTable.size());

    }

    @Test
    public void testPrint() {
        myTable.print();
    }

    @Test
    public void printTest1() {
        String[] columns = {"Hannah", "G", "Rocks", "HehHeh"};
        Table tester = new Table(columns);
        assertEquals(true, tester.add(new String[]{"3", "2", "3", "5"}));
        assertEquals(true, tester.add(new String[]{"1", "2", "3", "5"}));
        assertEquals(true, tester.add(new String[]{"2", "1", "3", "4"}));
        assertEquals(true, tester.add(new String[]{"1", "2", "3", "4"}));
        assertEquals(true, tester.add(new String[]{"2", "2", "3", "5"}));
        tester.print();
    }





}
