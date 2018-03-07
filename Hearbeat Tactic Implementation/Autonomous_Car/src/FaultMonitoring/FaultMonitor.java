package FaultMonitoring;
import VehicleControl.Receiver;

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
    private static final String LOGGING_FILE_PATH = "."+ File.separator +"src"+ File.separator + "Logging"+ File.separator +"failure_log.txt";

    /*Handles faults and logs failures*/
    public static void handleFault(String component, Receiver failedReceiver){
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        System.out.println("FaultMonitor: Sender failed");
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
        System.out.println("Starting BackupSender");
        ProcessBuilder backupsender_builder = new ProcessBuilder("java", "-cp",
                helper + File.separator + "out"+ File.separator +"production" + File.separator +"Autonomous_Car" + File.separator ,
                "Localization.BackupSender", String.valueOf(failedReceiver.getCurrentlocation()));
        backupsender_builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        Process backuplocalizationModule = null;
        try {
            backuplocalizationModule = backupsender_builder.start();
        } catch(IOException e){
            System.err.println("IOException: " + e.getMessage());
        }
        try {
            //this is for demonstration purposes since the reboot process would actually be fairly quick because
            //our Sender module is pretty light, in a more complex environment, the module would take more time to reboot
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Rebooting sender");
        ProcessBuilder sender_builder = new ProcessBuilder("java", "-cp",
                helper + File.separator + "out"+ File.separator +"production" + File.separator +"Autonomous_Car" + File.separator ,
                "Localization.Sender", String.valueOf(failedReceiver.getCurrentlocation()));
        sender_builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            Process localizationModule = sender_builder.start();
        } catch(IOException e){
            System.err.println("IOException: " + e.getMessage());
        }
        backuplocalizationModule.destroy();
        //Handle fault - Code to handle faults goes here - based on whatever component has failed

    }
}
