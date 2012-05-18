package player;

import library.interfaces.SongStream;

public interface PlayerListener {
	public void startedPlayback(SongStream stream);
	public void stoppedPlayback(SongStream stream);
	public void pausedPlayback(SongStream stream);
}
