import oscP5.*;

public class parseTest {
	static int recPort = 5000;
	float museCon;
	float calcCon;
	float alpha;
	float beta;
	float theta;
	boolean useable;
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
		
		/*
		while(true){
			System.out.println("From the muse: " + p.museCon);
			System.out.println("From calcs: " + p.calcCon);
		}*/
		
	}
	
	public void museCon(float f){
		//System.out.println("PLUG");
		//System.out.println("Plug for museCon: " + f);
		if(useable)
			museCon = f;
	}
	
	public void valid(int b){
		if(b != 0){
			useable = false;
		}
	}
	
	public void valid(int a, int b, int c, int d){
		if((a+b+c+d) != 4){
			useable = false;
		}
		else{
			useable = true;
		}
	}
	
	void oscEvent(OscMessage msg) {
		//System.out.println(msg);
		
		
		if(useable){	
			if (msg.checkAddrPattern("/muse/elements/alpha_absolute")==true) {  
				this.alpha = (msg.get(0).floatValue() + msg.get(1).floatValue() + msg.get(2).floatValue() + msg.get(3).floatValue())/4; 
			} 
			if (msg.checkAddrPattern("/muse/elements/beta_absolute")==true) {  
				this.beta = (msg.get(0).floatValue() + msg.get(1).floatValue() + msg.get(2).floatValue() + msg.get(3).floatValue())/4;
			} 
			if (msg.checkAddrPattern("/muse/elements/theta_absolute")==true) {  
				this.theta = (msg.get(0).floatValue() + msg.get(1).floatValue() + msg.get(2).floatValue() + msg.get(3).floatValue())/4;
				this.calcCon = (beta/(alpha+theta));
			} 
		}
	}
}
