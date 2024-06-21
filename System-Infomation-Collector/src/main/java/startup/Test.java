package startup;

import java.text.ParseException;
import net.redhogs.cronparser.*;

public class Test {
    public static void main(String[] args) throws ParseException {
        
        System.out.println(CronExpressionDescriptor.getDescription("0 5 5 * 2"));
    }
    
}
