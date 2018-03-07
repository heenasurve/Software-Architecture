package Localization;

import VehicleControl.ReceiverInterface;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


/**
 * Created by Heena on 2/15/2018.
 */
/*A Localization Module - Availability Critical*/
public class BackupSender {

    private final int HEARTBEAT_INTERVAL = 2000;
    private Registry registry;
    private ReceiverInterface receiver_stub;

    public void initialize() throws IOException, NotBoundException {

        /*get access to rmi registry once started*/
        registry = LocateRegistry.getRegistry();

        /*Lookup registry by name to access the stub - remote object of the monitoring component */
        receiver_stub = (ReceiverInterface) registry.lookup("ReceiverInterface");
    }

    public void sendHeartBeat(int location) throws IOException{
        Location current_location = new Location(location, Calendar.getInstance().getTime().getSeconds());

        while(true){
            try {
                /*something that this highly available module does here, may crash*/
                int current_time = Calendar.getInstance().getTime().getSeconds();
                current_location = getLocation(current_location.getCo_ords());

                        /* Justifying why we perform this division
                        we track the ratio of the time in seconds to ensure that location is retrieved within a second */
                double test = current_time/current_location.getTime();

                /*report status, by sending a heartbeat signal to monitoring module*/
                receiver_stub.readStatus(current_location.getCo_ords());
                System.out.println("BackupSender: I am alive.");
                /*wait for 2 seconds before sending the next heart beat signal*/
                Thread.sleep(HEARTBEAT_INTERVAL);

                /*Deliberately not catching the Arithmetic Exception - / by 0*/
            }catch(InterruptedException | RemoteException ex){
                System.out.println("BackupSender: " + ex.getMessage());
            }

        }
    }


    private static Location getLocation(int location){
        DecimalFormat df = new DecimalFormat("##.00");
        location+=1;
        int time_in_seconds = Calendar.getInstance().getTime().getSeconds();
        return new Location(location,time_in_seconds);
    }

    public static void main(String [] args){
        int initiallocation;

        if(args.length == 0 ){
            initiallocation = 0;
        }
        else{
            initiallocation = Integer.valueOf(args[0]);
        }
        BackupSender sender = new BackupSender();
        try{
            sender.initialize();
            Thread.sleep(2000);
            sender.sendHeartBeat(initiallocation);
        }catch(NotBoundException | IOException | InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("BackupSender initialized");
    }
}
