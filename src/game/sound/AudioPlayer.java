package game.sound;

import game.graphic.gui.model.VolumeModel;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer implements PlaybackListener {
	
	private static AudioPlayer player;
	public static AudioPlayer getPlayer() {
		if(player==null) player=new AudioPlayer();
		return player;
	}
	
	private Bgm bgm;
	private Collection<Sfx> sfx;
	private VolumeModel bgmVolume;
	private VolumeModel sfxVolume;
	
	public AudioPlayer() {
		sfx=Collections.synchronizedSet(new HashSet<Sfx>());
		bgmVolume=new VolumeModel();
		sfxVolume=new VolumeModel();
	}
	
	public void shutdown() {
		stopBgm();
		stopAllSfx();
	}
	
	public void setPause(boolean pause) {
		bgm.setPaused(pause);
	}

	public synchronized void playBgm(String name) {
		if (bgm!=null) stopBgm();
		try {
			bgm=new Bgm(name);
			bgmVolume.addAudio(bgm);
			bgm.play();
		} catch (UnsupportedAudioFileException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (LineUnavailableException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public synchronized void stopBgm() {
		try {
			bgmVolume.removeAudio(bgm);
			bgm.stop();
			bgm=null;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public synchronized void playSfx(String name) {
		try {
			Sfx s=new Sfx(name, this);
			sfxVolume.addAudio(s);
			sfx.add(s);
			s.play();
		} catch (UnsupportedAudioFileException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (LineUnavailableException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public synchronized void stopSfx(Sfx s) {
		try {
			sfxVolume.removeAudio(s);
			s.stop();
			sfx.remove(s);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void stopAllSfx() {
		for(Sfx s : sfx) {
			stopSfx(s);
		}
		
	}
	
	public VolumeModel getBgmVolumeModel() {
		return bgmVolume;
	}
	
	public VolumeModel getSfxVolumeModel() {
		return sfxVolume;
	}

	@Override
	public void playbackFinished(Audio source) {
		if(source instanceof Sfx) {
			stopSfx((Sfx)source);
		}
	}
}
