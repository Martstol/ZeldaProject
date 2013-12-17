package game.graphic.gui.model;

import game.sound.Audio;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class VolumeModel implements SliderModel {
	
	private Collection<Audio> audio;
	private int volume, maxValue, minValue, stepValue;
	
	public VolumeModel() {
		this(0.75, 0.005);
	}
	
	/**
	 * Constructs a volume model with custom default volume and step value.
	 * 
	 * @param defaultVolume Must be between 0 and 1
	 * @param stepValue Must be between 0 and 1
	 */
	public VolumeModel(double defaultVolume, double stepValue) {
		if(defaultVolume<0 || defaultVolume>1 || stepValue<0 || stepValue>1) {
			throw new RuntimeException("defaultVolume: "+defaultVolume+", "+"stepValue: "+stepValue);
		} else {			
			audio=Collections.synchronizedSet(new HashSet<Audio>());
			maxValue=Integer.MAX_VALUE/2-1;
			minValue=0;
			volume=(int)(defaultVolume*maxValue);
			this.stepValue=(int)(stepValue*maxValue);
		}
	}
	
	public void addAudio(Audio a) {
		audio.add(a);
		setAudioVolume(a);
	}
	
	public void removeAudio(Audio a) {
		audio.remove(a);
	}
	
	private void setAudioVolume(Audio a) {
		float r=volume/(float)(maxValue-minValue);
		float extent=a.getMaxVolume()-a.getMinVolume();
		a.setVolume(r*extent+a.getMinVolume());
	}
	
	@Override
	public void setValue(int newValue) {
		if (newValue<=minValue) {
			volume=minValue;
		} else if (newValue>=maxValue) {
			volume=maxValue;
		} else {
			volume=newValue;
		}
		for (Audio a : audio) {
			setAudioVolume(a);
		}
	}

	@Override
	public int getValue() {
		return volume;
	}

	@Override
	public int getMinValue() {
		return minValue;
	}

	@Override
	public int getMaxValue() {
		return maxValue;
	}

	@Override
	public int getStepValue() {
		return stepValue;
	}

	@Override
	public void increment() {
		setValue(volume+stepValue);
	}

	@Override
	public void decrement() {
		setValue(volume-stepValue);
	}


}
