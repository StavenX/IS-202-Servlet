package helpers;

import java.util.Calendar;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
/**
 *
 * @author adriannesvik
 */
public class getTime {
    public void calObj(PrintWriter out) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        out.println(df.format(calendar.getTime()));
    }
}
