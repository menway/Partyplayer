package player;

import songstreams.SongStream;


public interface PlayerListener {
	/**
	 * Is called, when a PlayerThread starts Playing
	 * @param stream - the SongStream that started playing in the PlayerThread
	 */
	public void startedPlayback(SongStream stream);
	/**
	 * Is called, when a PlayerThread stops playing. This is only called upon natural termination such as reaching the end of a file or stream.
	 * <b>Not</b> if a PlayerThread is stopped manually.
	 * @param stream - the SongStream that stopped playing in the PlayerThread
	 */
	public void stoppedPlayback(SongStream stream);
	/**
	 * Is called when a PlayerThread pauses playback.
	 * @param stream - The SongStream that paused in the PlayerThread
	 */
	public void pausedPlayback(SongStream stream);
}
