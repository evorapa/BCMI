package example;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
 
/**
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 *
 */
public class AudioPlayerExampleIntAcc implements LineListener {
     
    /**
     * this flag indicates whether the playback completes or not.
     */
    boolean playCompleted;
     
    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    public void play(String audioFilePath, int speed) {
        File audioFile = new File(audioFilePath);
 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
 
            AudioFormat format = audioStream.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            int playBackSpeed = speed;

            int frameSize = format.getFrameSize();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2^16];
            int read = 1;
            while( read>-1 ) {
                read = audioStream.read(b);
                if (read>0) {
                    baos.write(b, 0, read);
                }
            }
    
            byte[] b1 = baos.toByteArray();
            byte[] b2 = new byte[b1.length/playBackSpeed];
            for (int ii=0; ii<b2.length/frameSize; ii++) {
                for (int jj=0; jj<frameSize; jj++) {
                    b2[(ii*frameSize)+jj] = b1[(ii*frameSize*playBackSpeed)+jj];
                }
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(b2);
            AudioInputStream aisAccelerated = new AudioInputStream(bais, format, b2.length);

            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this);
 
            //audioClip.open(audioStream);
             
            audioClip.open(aisAccelerated);

            audioClip.start();
             
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
             
            audioClip.close();
            
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
         
    }
     
    /**
     * Listens to the START and STOP events of the audio line.
     */
    //@Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
 
    }
 
    public static void main(String[] args) {
        String audioFilePath = "D:/C Drive 2, C Drive's Revenge/Fun/Torrents/Music/Run The Jewels/RTJ2/11 Angel Duster.wav";
        AudioPlayerExampleIntAcc player = new AudioPlayerExampleIntAcc();
        player.play(audioFilePath, 1);
    }
 
}