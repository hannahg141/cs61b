
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain duplicate names. */
    Table(String[] columnTitles) {
        if (columnTitles.length == 0) {
            throw error("table must have at least one column");
        }
        _size = 0;
        _rowSize = columnTitles.length;

        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }

        _titles = columnTitles;
        _columns = new ValueList[columns()];
        _tableName = " ";

        for (int i = 0; i < columns(); i++) {
            _columns[i] = new ValueList();
        }
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return _titles.length;

    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return _titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        for (int i = 0; i < _columns.length; i++) {
            if (getTitle(i).equals(title)) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of rows in this table. */
    public int size() {
        return _size;

    }

    /** Return the value of column number COL (0 <= COL < columns())
     *  of record number ROW (0 <= ROW < size()). */
    public String get(int row, int col) {
        try {
            row = _index.get(row);
            return _columns[col].get(row);
        } catch (IndexOutOfBoundsException excp) {
            throw error("invalid row or column");
        }
    }

    /** Add a new row whose column values are VALUES to me if no equal
     *  row already exists.  Return true if anything was added,
     *  false otherwise. */
    public boolean add(String[] values) {
        if (values.length != columns()) {
            throw error("too many values!");
        }
        for (int r = 0; r < size(); r++) {
            int count = 0;
            for (int c = 0; c < columns(); c++) {
                if (get(r, c).equals(values[c])) {
                    count += 1;
                }
                if (count == columns()) {
                    return false;
                }
            }
        }


        for (int i = 0; i < columns(); i++) {
            String info = values[i];
            _columns[i].add(info);
        }
        _size += 1;
        int indexTracker = _index.size();
        for (int i = 0; i < indexTracker; i++) {
            if (compareRows(size() - 1, _index.get(i)) < 0) {
                _index.add(i, size() - 1);
                return true;
            }
        }
        if (_index.size() < size()) {
            _index.add(size() - 1, size() - 1);
        }
        return true;
    }

    /** Add a new row whose column values are extracted by COLUMNS from
     *  the rows indexed by ROWS, if no equal row already exists.
     *  Return true if anything was added, false otherwise. See
     *  Column.getFrom(Integer...) for a description of how Columns
     *  extract values. */
    public boolean add(List<Column> columns, Integer... rows) {
        String[] dataVal = new String[columns.size()];
        int i = 0;
        while (i < _rowSize) {
            dataVal[i] = columns.get(i).getFrom(rows);
            i++;
        }
        return add(dataVal);
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames);
            String details = input.readLine();
            while (details != null) {
                String[] data = details.split(",");
                table.add(data);
                details = input.readLine();
            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep = ",";
            String value = null;
            output = new PrintStream(name + ".db");
            for (int t = 0; t < columns(); t++) {
                value += getTitle(t);
                if (t + 1 != columns()) {
                    value += sep;
                } else {
                    value += "\n";
                }
            }
            for (int r = 0; r < size(); r++) {
                for (int c = 0; c < columns(); c++) {
                    value += get(r, c);
                    if (c + 1 != columns()) {
                        value += sep;
                    } else {
                        value += "\n";
                    }
                }
            }
            output.println(value.trim());

        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }


    /** Print my contents on the standard output, separated by spaces
     *  and indented by two spaces. */
    void print() {
        String details = "";
        for (int r = 0; r < size(); r++) {
            details += " ";
            for (int c = 0; c < columns(); c++) {
                details += get(r, c) + " ";
                if (c + 1 == columns()) {
                    details += "\n";
                }

            }
        }

        System.out.print(details);


    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
        List<Column> columns = new ArrayList<Column>();
        for (String name : columnNames) {
            Column col = new Column(name, this);
            columns.add(col);
        }
        ValueList oldTable = _columns[findColumn((columnNames.get(0)))];
        for (int row = 0; row < oldTable.size(); row++) {
            if (conditions == null) {
                result.add(columns, row);
            } else {
                if (Condition.test(conditions, row)) {
                    result.add(columns, row);
                }
            }
        }

        return result;
    }


    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
        List<Column> columns = new ArrayList<Column>();

        for (int i = 0; i < columnNames.size(); i++) {
            columns.add(new Column(columnNames.get(i), this, table2));
        }
        ArrayList<Column> columns1 = new ArrayList<>();
        ArrayList<Column> columns2 = new ArrayList<>();
        for (int i = 0; i < columns(); i++) {
            String name = this.getTitle(i);
            if (table2.findColumn(name) != -1) {
                columns1.add(new Column(name, this));
                columns2.add(new Column(name, table2));
            }
        }

        for (int row = 0; row < size(); row++) {
            for (int row2 = 0; row2 < table2.size(); row2++) {
                if (conditions == null) {
                    result.add(columns, row, row2);
                } else {
                    if (equijoin(columns1, columns2, row, row2)) {
                        if (Condition.test(conditions, row, row2)) {
                            result.add(columns, row, row2);
                        }
                    }
                }
            }
        }

        return result;
    }




    /** Return <0, 0, or >0 depending on whether the row formed from
     *  the elements _columns[0].get(K0), _columns[1].get(K0), ...
     *  is less than, equal to, or greater than that formed from elememts
     *  _columns[0].get(K1), _columns[1].get(K1), ....  This method ignores
     *  the _index. */
    private int compareRows(int k0, int k1) {
        for (int i = 0; i < _columns.length; i += 1) {
            int c = _columns[i].get(k0).compareTo(_columns[i].get(k1));
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 are indices, respectively,
     *  into those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
                                    int row1, int row2) {

        for (Column col1 : common1) {
            for (Column col2 : common2) {
                if (col1.getName().equals(col2.getName())) {
                    String value1 = col1.getFrom(row1);
                    String value2 = col2.getFrom(row2);
                    if (!value1.equals(value2)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /** A class that is essentially ArrayList<String>.  For technical reasons,
     *  we need to encapsulate ArrayList<String> like this because the
     *  underlying design of Java does not properly distinguish between
     *  different kinds of ArrayList at runtime (e.g., if you have a
     *  variable of type Object that was created from an ArrayList, there is
     *  no way to determine in general whether it is an ArrayList<String>,
     *  ArrayList<Integer>, or ArrayList<Object>).  This leads to annoying
     *  compiler warnings.  The trick of defining a new type avoids this
     *  issue. */
    private static class ValueList extends ArrayList<String> {
    }

    /** My column titles. */
    private final String[] _titles;
    /** My columns. Row i consists of _columns[k].get(i) for all k. */
    private final ValueList[] _columns;

    /** Rows in the database are supposed to be sorted. To do so, we
     *  have a list whose kth element is the index in each column
     *  of the value of that column for the kth row in lexicographic order.
     *  That is, the first row (smallest in lexicographic order)
     *  is at position _index.get(0) in _columns[0], _columns[1], ...
     *  and the kth row in lexicographic order in at position _index.get(k).
     *  When a new row is inserted, insert its index at the appropriate
     *  place in this list.
     *  (Alternatively, we could simply keep each column in the proper order
     *  so that we would not need _index.  But that would mean that inserting
     *  a new row would require rearranging _rowSize lists (each list in
     *  _columns) rather than just one. */
    private final ArrayList<Integer> _index = new ArrayList<>();

    /** My number of rows (redundant, but convenient). */
    private int _size;
    /** My number of columns (redundant, but convenient). */
    private final int _rowSize;
    /** the name of the table. */
    private String _tableName;
}
