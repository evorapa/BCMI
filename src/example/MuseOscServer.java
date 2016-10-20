package example;

import oscP5.*;

public class MuseOscServer {

	static MuseOscServer museOscServer;
	
	OscP5 museServer;
	static int recvPort = 5000;

	public static void main(String [] args) {
		museOscServer = new MuseOscServer();
		museOscServer.museServer = new OscP5(museOscServer, recvPort);
		System.out.println("Server is Started\n");
		 String audioFilePath = "D:/C Drive 2, C Drive's Revenge/Fun/Torrents/Music/Run The Jewels/RTJ2/11 Angel Duster.wav";
	     AudioPlayerExampleIntAcc player = new AudioPlayerExampleIntAcc();
	     player.play(audioFilePath);
			}
	
	void oscEvent(OscMessage msg) {
		if (msg.checkAddrPattern("/muse/elements/alpha_absolute")==true) {  
			System.out.println("### got a message " + msg);
			for(int i = 0; i < 4; i++) {
				System.out.print("EEG on channel " + i + ": " + msg.get(i).floatValue() + "\n"); 
			}
		} 
	}
}