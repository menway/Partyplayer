package player;

import java.io.UnsupportedEncodingException;

import songstreams.AbstractFileSongStream;
import library.interfaces.SongStream;

/**
 * A runnable that plays a SongStream
 * @author Joakim Reinert
 *
 */
public abstract class PlayerThread implements Runnable {
	
	/**
	 * The SongStream to play
	 */
	protected final SongStream stream;
	/**
	 * A PlayerListener that is informed of different states of the PlayerThread
	 */
	protected PlayerListener listener;

	protected boolean shouldStop;
	protected boolean shouldPause;
	/**
	 * Creates a new PlayerThread with the given arguments
	 * @param stream - the SongStream that should be played
	 * @param listener - the PlayerListener
	 */
	public PlayerThread(SongStream stream, PlayerListener listener) {
		this.stream = stream;
		this.listener = listener;
	}
	@Override
	public void run() {
		listener.startedPlayback(stream);
	}
	/**
	 * Returns an appropriate PlayerThread for the given SongStream
	 * @param stream - the SongStream that should be played
	 * @param listener - the PlayerListener
	 * @return a PlayerThread for the given arguments
	 */
	public static Runnable getThread(SongStream stream, PlayerListener listener) {
		try {
			if(stream instanceof AbstractFileSongStream)
				return new FilePlayerThread(stream, listener);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Stops the Playback of the SongStream
	 */
	public void stop() {
		shouldStop = true;
	}
	/**
	 * Pauses the Playback of the SongStream
	 */
	public void pause() {
		shouldPause = true;
	}
	/**
	 * Resumes the Playback of the SongStream
	 */
	public void resume() {
		shouldPause = false;
	}
 } 
