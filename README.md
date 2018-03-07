# Software-Architecture
1.	Run the Starter.java file 
2.	Observe the console to see the heart beat signals being sent with a timestamp (at a 2 second interval) – Should look like “Receiver: Received heartbeat signal at: 2018-02-22 17:50:24”
3.	When the critical component (Sender.java), stops sending heartbeat signals, the monitoring component (Receiver.java) prints out a message to the starndar outout
“Hearbeat interval exceeded - Localization Component failed - View log for details” to the console.
4. It then invokes a method on the FaultMonitor class that logs the failure and starts up a redundant Sender node called "BackupSender"
5. The FaultMonitor then attempts to reboot the (original) Sender and kills the BackupSender when the original is back up.

Expect the sender to fail randomly. You may have to re-run it to start at a different time and wait up to 60 seconds to see it fail.
