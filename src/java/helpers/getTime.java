package helpers;

import java.util.Calendar;
import java.io.PrintWriter;
/**
 *
 * @author adriannesvik
 */
public class getTime {
    public void calObj(PrintWriter out) {
        Calendar calendar = Calendar.getInstance();
        out.println(calendar.getTime());
    }
}
