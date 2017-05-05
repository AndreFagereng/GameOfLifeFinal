package model;


import org.codehaus.groovy.runtime.powerassert.SourceText;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andre on 24.04.2017.
 */
public class AudioPlaySound implements LineListener {


    /**
     * This audio is made by a friend, so all credits to Sindre Timberlid for
     * making this to us.
     */

    private String audioFilePath = "C:/Users/Jegern/IdeaProjects/GameOfLifeFinal/Resources/Sound/EightBit.wav";

    File file = new File(audioFilePath);
    AudioInputStream audioInputStream;
    AudioFormat audioFormat;
    DataLine.Info info;
    Clip clip;
    private FloatControl floatVolume;
    int lastFrame = 0;
    boolean playCompleted;

    /**
     * AudioPlaySound constructor
     *
     * Creates an object of AudioPlaySound class and tries to load
     * a audioFile(.wav) using the loadAudioFile method.
     *
     */

    public AudioPlaySound() {

        try {
            if (file != null) {
                loadAudioFile(file);
                System.out.println("Sound File Loaded");
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method loads a audioFile to the Game.
     *
     * @param file File is a Audio File.
     * @throws IOException Throws IOException.
     * @throws UnsupportedAudioFileException Throws UnsupportedAudioFileException.
     * @throws LineUnavailableException Throws LineUnavailableException.
     * @throws InterruptedException Throws InterruptedException.
     */

    protected void loadAudioFile(File file) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        try {

            audioInputStream = AudioSystem.getAudioInputStream(file);
            audioFormat = audioInputStream.getFormat();
            info = new DataLine.Info(Clip.class, audioFormat);
            clip = AudioSystem.getClip();


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Illegal Argument");
        }

        clip.open(audioInputStream);

        floatVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        floatVolume.setValue(-25.0f);
    }


    /**
     * This method gets the lastFrame position of the (audio) clip
     * and resumes the audio from where it stopped.
     */


    public void resume() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);

        if (clip != null && !clip.isRunning()) {

            if (lastFrame < clip.getFrameLength()) {
                clip.setFramePosition(lastFrame);
            } else {
                clip.setFramePosition(0);
            }
        }
        clip.start();
    }


    /**
     * This method stores the (audio) clip FramePosition in lastFrame
     * and pauses the audio.
     */

    public void pause() {
        if (clip != null && clip.isRunning()) {
            lastFrame = clip.getFramePosition();
            System.out.println(lastFrame);
            clip.stop();
        }

    }


    /**
     * This methods overrides the LineListener update method.
     *
     * Prints out when the playback has started and stopped.
     *
     * @param event LineEvent type.
     */


    @Override
    public void update(LineEvent event) {

        LineEvent.Type type = event.getType();


        if (type == LineEvent.Type.START) {
            System.out.println("Playback started");
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback complete");
        }
    }


}
