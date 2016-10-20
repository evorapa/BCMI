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
		 String audioFilePath = "C:/Users/zrgam_000/Downloads/02_-_No_Problem_feat_Lil_Wayne_and_2_Chainz.wav";
	     AudioPlayerExampleIntAcc player = new AudioPlayerExampleIntAcc();
	     player.play(audioFilePath, 1);
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