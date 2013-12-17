package game.sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class Audio implements Runnable {
	
	public static final String DEFAULT_PATH="/resources/audio/";
	public static final String BGM_PATH="bgm/";
	public static final String SFX_PATH="sfx/";
	public static final String DEFAULT_EXTENSION=".wav";
	
	protected Thread t;
	protected FloatControl volume;
	protected URL url; 
	protected AudioInputStream audioInput;
	protected SourceDataLine dataline;
	protected boolean isPlaying, isPaused;
	protected String name="not set";
	
	public Audio(URL url) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.url=url;
		audioInput=AudioSystem.getAudioInputStream(url);
		dataline=AudioSystem.getSourceDataLine(audioInput.getFormat());
		dataline.open();
		volume=(FloatControl)dataline.getControl(FloatControl.Type.MASTER_GAIN);
		isPlaying=false;
		isPaused=false;
	}
	
	public synchronized void play() {
		isPlaying=true;
		t=new Thread(this, "Audio playback: "+name);
		dataline.start();
		t.start();
	}
	
	public synchronized void stop() throws IOException {
		isPlaying=false;
		dataline.stop();
		dataline.close();
		audioInput.close();
	}
	
	public synchronized boolean isPlaying() {
		return isPlaying;
	}
	public synchronized void setVolume(float newValue) {
		if (newValue>=volume.getMinimum() && newValue<=volume.getMaximum()) {
			volume.setValue(newValue);
		} else if (newValue<volume.getMinimum()) {
			volume.setValue(volume.getMinimum());
		} else if (newValue>volume.getMaximum()) {
			volume.setValue(volume.getMaximum());
		}
	}
	
	public synchronized float getVolume() {
		return volume.getValue();
	}
	
	public synchronized float getMaxVolume() {
		return volume.getMaximum();
	}
	
	public synchronized float getMinVolume() {
		return volume.getMinimum();
	}
	
	public synchronized void setPaused(boolean paused) {
		isPaused=paused;
	}
	
	public synchronized boolean isPaused() {
		return isPaused;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
