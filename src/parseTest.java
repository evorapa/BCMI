import oscP5.*;

public class parseTest {
	static int recPort = 5000;
	float museCon;
	float calcCon;
	OscP5 museServer;
	
	static parseTest parser;
	
	public static void main(String args[]){
		parseTest p = new parseTest();
		p.museServer = new OscP5(p, recPort);
		System.out.println("Server Started");
		p.museServer.plug(p, "museCon", "/muse/elements/experimental/concentration");
		p.museServer.plug(p, "valid", "/muse/elements/blink");
		p.museServer.plug(p, "valid", "/muse/elements/jaw_clench");
		p.museServer.plug(p, "valid", "/muse/elements/is_good");
		
	}
	
	public void museCon(float f){
		System.out.println("PLUG");
		System.out.println("Plug for museCon: " + f);
	}
	
	public void valid(int b){
		System.out.println("PLUGGED");
		System.out.println("Plug for blink/jaw_clench: " + b);
	}
	
	public void valid(int a, int b, int c, int d){
		System.out.println("PLUGGED");
		System.out.println("Plug for is_good: " + a + " " + b + " " + c + " " + d);
	}
	
	void oscEvent(OscMessage msg) {
		//System.out.println(msg.isPlugged());
		/*
		if (msg.checkAddrPattern("/muse/elements/experimental/concentration")==true && !msg.isPlugged()) {  
			this.museCon = msg.get(0).floatValue();
			System.out.println("### got a message " + msg);
			System.out.print("Concentration: " + museCon + "\n"); 
			
		} 
		*/
	}
}
