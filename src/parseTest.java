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
		p.museServer.plug(parser, "museCon", "/muse/elements/experimental/concentration");
		p.museServer.plug(parser, "valid", "/muse/elements/blink");
		p.museServer.plug(parser, "valid", "/muse/elements/jaw_clench");
		p.museServer.plug(parser, "valid", "/muse/elements/is_good");
		
	}
	
	public void museCon(float f){
		System.out.println("Plug for museCon: " + f);
	}
	
	public void valid(int b){
		System.out.println("Plug for blink/jaw_clench: " + b);
	}
	
	public void valid(int a, int b, int c, int d){
		System.out.print("plug for is_good " + a + " " + b + " " + c + " " + d);
	}
	
	void oscEvent(OscMessage msg) {
		if (msg.checkAddrPattern("/muse/elements/experimental/concentration")==true) {  
			this.museCon = msg.get(0).floatValue();
			System.out.println("### got a message " + msg);
			System.out.print("Concentration: " + msg.get(0).floatValue() + "\n"); 
			
		} 
	}
}
