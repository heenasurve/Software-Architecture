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
public class Sender {

   private final int HEARTBEAT_INTERVAL = 2000;
   private Registry registry;
   private ReceiverInterface receiver_stub;

    public void initialize() throws IOException, NotBoundException {

        /*get access to rmi registry once started*/
            registry = LocateRegistry.getRegistry();

        /*Lookup registry by name to access the stub - remote object of the monitoring component */
            receiver_stub = (ReceiverInterface) registry.lookup("ReceiverInterface");
            System.out.println("Done");

    }

    public void sendHeartBeat() throws IOException{

        Thread sender_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        /*something that this highly available module does here, may crash*/
                        int current_time = Calendar.getInstance().getTime().getSeconds();
                        Location current_location = getLocation();

                        /* Justifying why we perform this division
                        we track the ratio of the time in seconds to ensure that location is retrieved within a second */
                        double test = current_time/current_location.getTime();

                        /*report status, by sending a heartbeat signal to monitoring module*/
                        receiver_stub.readStatus();

                        /*wait for 2 seconds before sending the next heart beat signal*/
                        Thread.sleep(HEARTBEAT_INTERVAL);

                        /*Deliberately not catching the Arithmetic Exception - / by 0*/
                    }catch(InterruptedException | RemoteException ex){
                        System.out.println(ex.getMessage());
                    }

                }
            }
        });
        /*start process */
        System.out.println("Started sender thread");
        sender_thread.start();


        System.out.println("\n Localization module sent the first heartbeat signal at : "+
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }


    private static Location getLocation(){
        DecimalFormat df = new DecimalFormat("##.00");
        String mylocation = df.format(1 + Math.random() * (100 - 1)) + ":" +df.format(1 + Math.random() * (100 - 1));
        int time_in_seconds = Calendar.getInstance().getTime().getSeconds();
        return new Location(mylocation,time_in_seconds);
    }

    public static void main(String [] args) throws NotBoundException,IOException {
        Sender sender = new Sender();
        sender.initialize();
        sender.sendHeartBeat();
        System.out.println("sender initialized");
    }
}
