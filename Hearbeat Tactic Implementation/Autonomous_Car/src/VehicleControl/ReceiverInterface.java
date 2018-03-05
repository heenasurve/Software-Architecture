package VehicleControl;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Heena on 2/18/2018.
 */
public interface ReceiverInterface extends Remote{

    /*Starts up the receiver and sets up mechanism for critical components to report health */
    void initializeReceiver() throws RemoteException;

    /* Remote method invoked by any highly available module to send a hearbeat signal/status*/
    void readStatus() throws RemoteException;

    /*Monitors the health of the localization module every seconds */
    void monitorLocalizationModule() throws RemoteException;
}
