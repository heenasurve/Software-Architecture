package RemoteStarter;

import Localization.Sender;
import VehicleControl.Receiver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Heena on 2/15/2018.
 */

/*Starts the car*/
public class Starter {

    public static void main(String [] args){
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        try {

            ProcessBuilder pb = new ProcessBuilder("rmiregistry");
            pb.directory(new File("." + File.separator +"out" + File.separator +"production"+ File.separator +"Autonomous_Car"));
            //pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.start();

            Thread.sleep(1000);
            System.out.println("Starting receiver");
            ProcessBuilder receiver_builder = new ProcessBuilder("java" , "-cp",
                    helper + File.separator + "out"+ File.separator +"production" + File.separator +"Autonomous_Car" + File.separator ,
                    "VehicleControl.Receiver");
            receiver_builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process vehicleControlModule = receiver_builder.start();
            Thread.sleep(1000);
            System.out.println("Starting sender");
            ProcessBuilder sender_builder = new ProcessBuilder("java", "-cp",
                    helper + File.separator + "out"+ File.separator +"production" + File.separator +"Autonomous_Car" + File.separator ,
                    "Localization.Sender");
            sender_builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process localizationModule = sender_builder.start();

            InputStream errors_receiver = vehicleControlModule.getErrorStream();
            String err_r = "Receiver - ";
            if(vehicleControlModule.getErrorStream().read() !=-1){
                for (int i = 0; i < errors_receiver.available(); i++) {
                    err_r += (char)errors_receiver.read();
                }
                System.out.println(err_r);
            }else{
                System.out.println(vehicleControlModule.getOutputStream());
            }

            InputStream errors_sender = localizationModule.getErrorStream();
            String err_s = "Sender - ";
            if(localizationModule.getErrorStream().read() == -1) {
                for (int i = 0; i < errors_sender.available(); i++) {
                    err_s += (char) errors_sender.read() + " ";
                }
                System.out.println(err_s);
            }else{
                OutputStream out = localizationModule.getOutputStream();
                System.out.println(out);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
