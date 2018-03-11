# Fault Monitoring and Recovery simulation

<html>
<body>

<h3> The implementation simulates architectural tactics for components seen in an autonomous car </h3>

<p> The highly available module in the implementation, as would be in an autonomous car is the Localization Module </p>
<p> The monitoring component is the Vehicle Control Module (which would use location information from the localization component) </p> 
<p> Faults are logged and handled by the Fault Monitor.</p> 


<br>

<h4> Fault Monitoring is simulated using the Heartbeat tactic </h4>
<ul>
  <li>Here the highly available module sends an intermittent signal to the monitoring component to ensure it is alive alongside the information it sends. </li>
  <li>The monitoring process continues to listen for signals at a fixed montioring interval from this highly available component. </li>
  <li>It reports any missed heartbeat signals to a fault manager which logs the failure.</li>
</ul>

<br><br>

<h4> Fault Recovery is simulated using the Cold Sparing (a Redundancy technique to ensure availability) </h4>
<ul>
<li> When the monitoring process does not receive a heartbeat signal, in the stipulated monitoring interval (plus an added buffer period),it reports failure and sends the Fault Manager information last received from the highly available component.</li>
<li>The Fault Monitor logs the failure, starts up a backup module by forwarding the information it received from the monitoring component.</li>
<li>Meawhile it also attempts to reboot the original module while keeping track of the information it would need.
Once the original component is back up the redundant node or cold spare is shut down.</li>
</ul>


1.	Run the Starter.java file.
2.	Observe the standard output to see the heartbeat signals being sent with a timestamp (at a fixed interval) – Should look like “Receiver: Received heartbeat signal at: 2018-02-22 17:50:24”.
3.	When the critical component (Sender.java in the Localization package), stops sending heartbeat signals, the monitoring component (Receiver.java in the VehicleControl package) prints out a message to the standard output.
“Hearbeat interval exceeded - Localization Component failed - View log for details” to the console.
4. It then invokes a method on the FaultMonitor class that logs the failure and starts up a redundant Sender node called "BackupSender"
5. The FaultMonitor then attempts to reboot the (original) Sender and kills the BackupSender when the original is back up.

<i>Expect the sender to fail randomly. You may have to re-run it to start at a different time and wait up to 60 seconds to see it fail.</i>

</body>
</html>
