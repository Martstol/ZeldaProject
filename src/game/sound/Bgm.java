package game.sound;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bgm extends Audio {
	
	private boolean running;

	public Bgm(String name) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		super(Audio.class.getResource(DEFAULT_PATH+BGM_PATH+name+DEFAULT_EXTENSION));
		this.name=name;
		this.running=false;
	}
	
	@Override
	public synchronized void play() {
		running=true;
		super.play();
	}
	
	@Override
	public synchronized void stop() throws IOException {
		running=false;
		super.stop();
	}

	@Override
	public void run() {
		try {
			while(running) {
				int nBytesRead=0;
				byte sampledData[]=new byte[dataline.getBufferSize()];
				while(nBytesRead!=-1 && isPlaying) {
					if(!isPaused) {						
						nBytesRead=audioInput.read(sampledData, 0, dataline.getBufferSize());
						if(nBytesRead>=0) {
							dataline.write(sampledData, 0, nBytesRead);
						}
					} else {
						Thread.yield();
					}
				}
				audioInput.close();
				audioInput=AudioSystem.getAudioInputStream(url);
			}
			dataline.drain();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

}
