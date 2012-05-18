package player;

import java.io.UnsupportedEncodingException;

import songstreams.AbstractFileSongStream;
import library.interfaces.SongStream;

public abstract class PlayerThread implements Runnable {
	
	protected final SongStream stream;
	protected PlayerListener listener;
	protected boolean shouldStop;
	
	public PlayerThread(SongStream stream, PlayerListener listener) {
		this.stream = stream;
		this.listener = listener;
	}
	@Override
	public void run() {
		listener.startedPlayback(stream);
	}
	public static Runnable getThread(SongStream stream, PlayerListener listener) {
		try {
			if(stream instanceof AbstractFileSongStream)
				return new FilePlayerThread(stream, listener);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void stop() {
		shouldStop = true;
	}
}
