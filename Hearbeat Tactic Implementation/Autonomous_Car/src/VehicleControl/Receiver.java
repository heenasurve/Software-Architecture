package VehicleControl;

import FaultMonitoring.FaultMonitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by Heena on 2/15/2018.
 */
public class Receiver implements ReceiverInterface{


    private static final int MONITORING_INTERVAL = 5000;
    private static final String REGISTRY_HOST = "localhost";
    private static long lastHeartbeatTime;


    public Receiver() {}

    public void initializeReceiver(){
       ReceiverInterface receiver_stub;
       Registry registry;
        try{


            /*create and export remote object*/
            Receiver receiver = new Receiver();

            receiver_stub = (ReceiverInterface)
                    UnicastRemoteObject.exportObject(receiver,0);

            /*register remote object with registry*/
            registry = LocateRegistry.getRegistry(REGISTRY_HOST);
            registry.bind("ReceiverInterface", receiver_stub);

        }catch(Exception e){
            System.out.println(" Receiver Exception : " + e.getMessage());
            FaultMonitor.handleFault("Vehicle Control");
            e.printStackTrace();
        }
    }

    /* monitors the time difference between the last 2 heart beat signals */
    @Override
    public void monitorLocalizationModule() throws RemoteException {

        Thread receiver_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    if (!isAlive()) {
                        System.out.println("Hearbeat interval exceeded - Localization Component failed - View log for details");
                        FaultMonitor.handleFault("Localization");
                    }
                }
            }
        });

        receiver_thread.start();
    }

    private boolean isAlive(){
        long interval = System.currentTimeMillis() - lastHeartbeatTime;
        int error = 100; //100ms error tolerable
        return interval <= (MONITORING_INTERVAL + error);
    }

    /*---------------THE REMOTE METHOD--------------
     Receives heart beat messages from the monitored component*/
    public void readStatus() throws RemoteException{
        lastHeartbeatTime = System.currentTimeMillis();
        System.out.println("Received heartbeat signal at : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static void main(String [] args){
        Receiver receiver = new Receiver();
        receiver.initializeReceiver();
        try{
            receiver.monitorLocalizationModule();
        }catch(Exception ex){
            System.out.println("Vehicle control - receiver exception  - " + ex.getMessage());
        }
    }



}