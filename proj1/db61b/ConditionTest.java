package db61b;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ConditionTest {

    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(ConditionTest.class));


    }

    Table myTable;
    Column _col1, _col2;
    Condition less, greater, eq, lessEq, greaterEq, notEq, less2;



    private String[] columnTitles =
            new String[] {"SID", "Lastname",
                          "Firstname", "SemEnter", "YearEnter", "Major"};
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

        _col1 = new Column(columnTitles[1], myTable);
        _col2 = new Column(columnTitles[2], myTable);

        less = new Condition(_col1, "<", _col2);
        less2 = new Condition(_col2, "<", _col1);
        greater = new Condition(_col1, ">", _col2);
        eq = new Condition(_col1, "=", _col2);
        lessEq = new Condition(_col1, "<=", _col2);
        greaterEq = new Condition(_col1, ">=", _col2);
        notEq = new Condition(_col1, "!=", _col2);

    }


    @Test
    public void testTest() {
        assertTrue(less2.test(0));
        assertFalse(less.test(0));
        assertFalse(eq.test(0));
        assertTrue(notEq.test(0));
        assertTrue(greaterEq.test(0));
        assertFalse(lessEq.test(0));




    }
}


