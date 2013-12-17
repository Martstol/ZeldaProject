package game.sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sfx extends Audio {
	
	private PlaybackListener listener;

	public Sfx(String name, PlaybackListener listener) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		super(Audio.class.getResource(DEFAULT_PATH+SFX_PATH+name+DEFAULT_EXTENSION));
		this.name=name;
		this.listener=listener;
	}

	@Override
	public void run() {
		try {
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
			dataline.drain();
			listener.playbackFinished(this);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
