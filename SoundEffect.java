import javax.sound.sampled.*;
import java.io.*;
/**
 * SoundEffect class used to create SoundEffect objects that can play a sound file. This class is part of my bonus to the Tic Tac Toe assignment3. 
 * The purpose of this class is to add the functionality for playing sounds when things happen in the tic tac toe game.
 * NOTE: I tried to implement sounds the way that was presented in the example sound.zip, however I was unable to get it working the way it was shown in
 * the example. Becuase of this, I did some research on other ways to implement sounds and came across the javax.sound.sampled library which used 
 * in my SoundEffect class
 *
 * @author George Papoutsis
 * @version April 5, 2023
 */
public class SoundEffect
{
    /* the sound clip we want to play */
    private Clip soundClip;
    
    /**
     * The constructor for the SoundEffect class used to create SoundEffect objects.
     * 
     * @param fileName - Name of the file we want to play.
     */
    public SoundEffect(String fileName){
        try{
            File file = new File(fileName); //Create the file
            AudioInputStream s = AudioSystem.getAudioInputStream(file);
            soundClip = AudioSystem.getClip(); // get the clip that can be used for playing the sound
            soundClip.open(s); //open the clip
        }catch(Exception e){
            //Stop any runtime errors
        }
    }
    
    /**
     * The play method for the SoundEffect class used to play the desired audio file.
     * 
     */
    public void play(){
        soundClip.setFramePosition(0); // set position to the beginning of the file
        soundClip.start(); // start playing
    }
}
