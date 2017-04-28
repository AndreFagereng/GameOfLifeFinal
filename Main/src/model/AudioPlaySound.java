package model;


import org.codehaus.groovy.runtime.powerassert.SourceText;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andre on 24.04.2017.
 */
public class AudioPlaySound implements LineListener {


    private String audioFilePath = "../GameOfLifeFinal/Resources/Sound/ChillingMusic.wav";

    File file = new File(audioFilePath);
    AudioInputStream audioInputStream;
    AudioFormat audioFormat;
    DataLine.Info info;
    Clip clip;
    private FloatControl floatVolume;
    int lastFrame = 0;

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

    protected void loadAudioFile(File file) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        try {

            audioInputStream = AudioSystem.getAudioInputStream(file);
            audioFormat = audioInputStream.getFormat();
            info = new DataLine.Info(Clip.class, audioFormat);
            clip = AudioSystem.getClip();


        } catch (IllegalArgumentException e) {
            System.out.println("Illegal Arguemnt");
        }

        clip.open(audioInputStream);

        floatVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        floatVolume.setValue(-25.0f);
    }

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

    public void pause() {
        if (clip != null && clip.isRunning()) {
            lastFrame = clip.getFramePosition();
            System.out.println(lastFrame);
            clip.stop();
        }

    }


    boolean playCompleted;

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
