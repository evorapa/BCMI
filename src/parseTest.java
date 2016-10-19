import oscP5.*;

public class parseTest {
	static int recPort = 5000;
	int count = 0;
	int limit = 5;
	float museCon;
	float calcCon;
	float alpha;
	float beta;
	float theta;
	float[] history;
	boolean useable;
	OscP5 museServer;
	
	static parseTest parser;
	
	public void setup(){
		museServer = new OscP5(this, recPort);
		System.out.println("Server Started");
		museServer.plug(this, "museCon", "/muse/elements/experimental/concentration");
		museServer.plug(this, "valid", "/muse/elements/blink");
		museServer.plug(this, "valid", "/muse/elements/jaw_clench");
		museServer.plug(this, "valid", "/muse/elements/is_good");
	}
	
	public static void main(String args[]){
		
		parseTest p = new parseTest();
		/*
		p.setup();
		
		while(true){
			System.out.println("From the muse: " + p.museCon);
			System.out.println("From calcs: " + p.calcCon);
		}
		
		*
		*Space for setting up some class audio player
		*We can write methods to alter it as we read in data
		*
		*/
		
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
				
				history[count] = this.calcCon;
				count++;
			}
		}
		
		if(count == limit){
			count = 0;
			this.calcCon = ewma(history); //Or whatever we decide to do with the data.
			//Then we pass the calculation to some audio modifier
		}
	}
	
	public static float ewma (float[] z){

		float lam = (float) (1-(2.0/(z.length+1)));
		
		float smoothed = 0;
		
		for(int i = 0; i < z.length; i ++){
			smoothed += z[i];
		}
		
		smoothed = smoothed/z.length;
		
	    for(int i = 0; i < z.length; i++){
	    	smoothed = lam*z[i] + (1-lam)*smoothed;
	    }
	    
	    return smoothed;
	}
}
