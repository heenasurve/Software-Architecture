package FaultMonitoring;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Heena on 2/18/2018.
 */
public class FaultMonitor {
    private String component;
    private static final String LOGGING_FILE_PATH = "./src/Logging/failure_log.txt";

    /*Handles faults and logs failures*/
    public static void handleFault(String component){
        //Log failure
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(LOGGING_FILE_PATH,true);
            fw.write("\n\n" + component + " component failed on : " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")));
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }finally {
            try {
                fw.close();
            }catch (IOException | NullPointerException io){
                System.err.println("IOException: " + io.getMessage());
            }
        }
        //Handle fault - Code to handle faults goes here - based on whatever component has failed

    }
}
