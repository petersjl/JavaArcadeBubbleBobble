import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {

	private int contactWait;

	private long pauseTime;
	private boolean paused;

	/**
	 * Index - Sound | 0 - background music | 1 - contact | 2 - powerup | 3 - fruit
	 * ding | 4 - bubble pop | 5 - jump whoosh | 6 - fireball start | 7 - fireball
	 * end
	 */
	private ArrayList<Clip> audioClips = new ArrayList<Clip>();
	private ArrayList<AudioInputStream> audioStreams = new ArrayList<AudioInputStream>();

	public Music() {
		contactWait = -1;

		ArrayList<Object> temp;
		String[] list = {
				"fight-dance-epic-action-music-instrumental-dramatic-intense-heroic-movie-film-soundtracks.wav",
				"Swords_Collide-Sound_Explorer-2015600826.wav", "Power_Up_Ray-Mike_Koenig-800933783.wav",
				"tpirding.wav", "Pop Banner-SoundBible.com-641783855.wav", "spin_jump-Brandino480-2020916281.wav",
				"Small Fireball-SoundBible.com-1381880822.wav", "Flame Arrow-SoundBible.com-618067908.wav" };
		for (int i = 0; i < list.length; i++) {
			temp = this.makeSound(list[i]);
			audioStreams.add((AudioInputStream) (temp.get(0)));
			audioClips.add((Clip) temp.get(1));

		}
	}

	/**
	 * Returns an arary containing the music information to create a playable
	 * variable
	 * 
	 * @param filename
	 * @return audio stream , clip
	 */
	public ArrayList<Object> makeSound(String filename) {
		File audioFile3 = new File("./Music/" + filename);
		AudioInputStream audioStreamTemp = null;
		try {
			audioStreamTemp = AudioSystem.getAudioInputStream(audioFile3);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AudioFormat format3 = audioStreamTemp.getFormat();
		DataLine.Info info3 = new DataLine.Info(Clip.class, format3);
		Clip audioClipTemp = null;
		try {
			audioClipTemp = (Clip) AudioSystem.getLine(info3);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Object> temp = new ArrayList<Object>();
		temp.add(audioStreamTemp);
		temp.add(audioClipTemp);
		return temp;
	}

	/**
	 * Plays the sound given a respective index
	 * 
	 * @param index
	 */
	public void playSound(int index) {
		if (audioClips.get(index).isOpen()) {
			audioClips.get(index).stop();
			audioClips.get(index).setMicrosecondPosition(1);
			audioClips.get(index).start();
		} else {
			try {
				audioClips.get(index).open(audioStreams.get(index));
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			audioClips.get(index).start();
		}
	}

	/**
	 * plays the background music again when it finishes
	 */
	public void update() {
		if (((double) audioClips.get(0).getMicrosecondLength() - audioClips.get(0).getMicrosecondPosition())
				/ audioClips.get(0).getMicrosecondLength() < .01) {
			playSound(0);
		}
	}

	public void decreaseTime() {
		if (this.contactWait > 0)
			this.contactWait--;
		else {
		}
	}

	// --------------------------------------------------------------------------------
	// Various sound sequences

	public void music() {
		playSound(0);
	}

	public void pauseMusic() {
		if (!paused) {
			paused = true;
			this.pauseTime = audioClips.get(0).getMicrosecondPosition();
			audioClips.get(0).stop();
		} else {
			paused = false;
			audioClips.get(0).setMicrosecondPosition(this.pauseTime);
			audioClips.get(0).start();
		}
	}

	public void contact() {
		playSound(1);
	}

	public void powerUp() {
		playSound(2);
	}

	public void ding() {
		playSound(3);
	}

	public void makePop() {
		playSound(4);
	}

	public void pop() {
		playSound(4);
	}

	public void whoosh() {
		playSound(5);
	}

	public void fireBallStart() {
		playSound(6);
	}

	public void fireBallEnd() {
		playSound(7);
	}
}
