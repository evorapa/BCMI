import oscP5.*;
import example.*;

public class parseTest {
	static int recPort = 5000;
	int count = 0;
	int limit;
	float museCon = 0;
	float calcCon;
	float alpha;
	float beta;
	float theta;
	float[] history;
	boolean useable;
	OscP5 museServer;
	
	static parseTest parser;
	
	parseTest(){
		limit = 5;
		history = new float[limit];
	}
	
	parseTest(int limit){
		this.limit = limit;
		history = new float[limit];
	}
	
	public void setup(){
		museServer = new OscP5(this, recPort);
		System.out.println("Server Started");
		//museServer.plug(this, "museCon", "/muse/elements/experimental/concentration");
		museServer.plug(this, "valid", "/muse/elements/blink");
		museServer.plug(this, "valid", "/muse/elements/jaw_clench");
		museServer.plug(this, "valid", "/muse/elements/is_good");
	}
	
	public static void main(String args[]){
		int speed = 1;
		parseTest p = new parseTest();
		p.setup();
		String audioFilePath = "C:/Users/zrgam_000/Downloads/looperman-l-0173301-0100643-eendee-piano-nostalgia.wav";
	    while(true){
		    AudioPlayerExampleIntAcc player = new AudioPlayerExampleIntAcc();
	    	player.play(audioFilePath, speed);
	    	
	    	if(Math.abs(p.museCon) > 0.5){
	    		speed = 2;
	    	}
	    	else{
	    		speed = 1;
	    	}
	    }
	}
	
	public void valid(int b){
		////System.out.println("Blinks");
		if(b != 0){
			//System.out.print("NOT");
			useable = false;
		}
	}
	
	public void valid(int a, int b, int c, int d){
		//System.out.println("good");

		if((a+b+c+d) != 4){
			System.out.println("NOT " + " " + a+ " "+b+ " "+c+ " "+d);
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
				//System.out.println("Alpha " + count + ": " + this.alpha);
			} 
			if (msg.checkAddrPattern("/muse/elements/beta_absolute")==true) {  
				//System.out.println("Beta " + count + ": " + this.beta);
				this.beta = (msg.get(0).floatValue() + msg.get(1).floatValue() + msg.get(2).floatValue() + msg.get(3).floatValue())/4;
			} 
			if (msg.checkAddrPattern("/muse/elements/theta_absolute")==true) {  
				this.theta = (msg.get(0).floatValue() + msg.get(1).floatValue() + msg.get(2).floatValue() + msg.get(3).floatValue())/4;
				//System.out.println("Theta " + count + ": " + this.theta);
				this.calcCon = (beta/(alpha+theta));
				
				if(count == 0 || !( Math.abs(history[count-1] - this.calcCon) > Math.abs(history[count-1]*10) )){
					//System.out.println("calcCon " + count + ": " + this.calcCon);
					history[count] = this.calcCon;
					count++;
				}
			}
		}
		
		if(count == limit){
			count = 0;
			this.museCon = this.ewma(history); 
			
			System.out.println(this.museCon);
			
			//System.out.println("Alter music here");
			
			//Or whatever we decide to do with the data.
			//Then we pass the calculation to some audio modifier
		}
	}
	
	public float ewma (float[] z){

		float lam = (float) (1-(2.0/(z.length+1)));
		
		float smoothed = museCon;
		
	    for(int i = 0; i < z.length; i++){
	    	smoothed = lam*z[i] + (1-lam)*smoothed;
	    }
	    
	    return smoothed;
	}
}
