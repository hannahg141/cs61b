import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author hannahgrossman
 */
public class TrReader extends Reader {
    /**
     * A new TrReader that produces the stream of characters produced
     * by STR, converting all characters that occur in FROM to the
     * corresponding characters in TO.  That is, change occurrences of
     * FROM.charAt(0) to TO.charAt(0), etc., leaving other characters
     * unchanged.  FROM and TO must have the same length.
     */
    private Reader _tReader;
    /**
     * i guess i need a comment on this line.
     * hmmm still failing the test
     */
    private String _from;

    /**
     * my variable of what to copy over.
     */
    private String _to;

    /**
     * str is some random reader to be translated.
     *
     * @param str  the reader
     * @param from from
     * @param to   to!
     *             passes in and translates characters.
     */
    public TrReader(Reader str, String from, String to) {
        _tReader = str;
        _from = from;
        _to = to;
    }

    @Override
    public void close() throws IOException {
        try {
            _tReader.close();
        } catch (IOException e) {
            return;
        }


    }


    @Override
    public int read() throws IOException {
        return super.read();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int counter = _tReader.read(cbuf, off, len);
        int i = off;
        while (i < off + len) {
            int mine = _from.indexOf(cbuf[i]);
            if (mine != -1) {
                cbuf[i] = _to.charAt(mine);
            } else {
                cbuf[i] = cbuf[i];
            }
            i++;

        }
        return Math.min(counter, len);

    }
}

