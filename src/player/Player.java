package player;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import library.interfaces.SongStream;

/**
 * A queued player for SongStreams
 * @author Joakim Reinert
 *
 */
public class Player implements PlayerListener {
	
	/**
	 * The currently playing SongStream
	 */
	private SongStream nowPlaying;
	
	private final PlayerExecutor playerExecutor;
	
	/**
	 * The queue containing all SongStreams that are scheduled to play
	 */
	private final List<SongStream> queue;
	
	/**
	 * A Stack containing all previously played SongStreams
	 */
	private final Stack<SongStream> lastPlayed;
	private boolean isPaused;
	private boolean isStopped;
	
	public Player() {
		playerExecutor = new PlayerExecutor();
		queue = new LinkedList<SongStream>();
		lastPlayed = new Stack<SongStream>();
		isStopped = true;
	}
	
	/**
	 * Adds a SongStream to the tail of the queue
	 * @param stream - the SongStream to be queued
	 */
	public void queue(SongStream stream) {
		queue.add(stream);
	}
	/**
	 * Starts playing SongStreams from the head of the queue
	 */
	public void play() {
		if(isPaused)
			playerExecutor.resume();
		else {
			if(isStopped) {
				playerExecutor.execute(PlayerThread.getThread(queue.get(0), this));
				playerExecutor.resume();
			}
		}
		isPaused = false;
		isStopped = false;
	}
	/**
	 * Pauses the playback of the currently playing SongStream
	 */
	public void pause() {
		playerExecutor.pause();
		isPaused = true;
	}
	
	/**
	 * Stops the playback of the currently playing SongStream (not resumable)
	 */
	public void stop() {
		playerExecutor.stop();
		isStopped = true;
	}
	/**
	 * Skips the currently playing SongStream and moves to the next SongStream in the queue.
	 */
	public void skip() {
		stop();
		lastPlayed.push(queue.remove(0));
		play();
	}
	/**
	 * Skips the currently playing SongStream and starts playing the first SongStream in the lastPlayed stack
	 */
	public void back() {
		stop();
		queue.add(0, lastPlayed.pop());
		play();
	}
	/**
	 * Plays the given SongStream immediately, skipping the queue and stopping the playback of the currently playing SongStream
	 * @param stream
	 */
	public void playNow(SongStream stream) {
		stop();
		lastPlayed.push(queue.get(0));
		queue.set(0, stream);
		play();
	}
	
	@Override
	public void startedPlayback(SongStream stream) {
		nowPlaying = stream;
	}
	@Override
	public void stoppedPlayback(SongStream stream) {
		skip();
	}
	@Override
	public void pausedPlayback(SongStream stream) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 * @return the currently playing SongStream
	 */
	public SongStream getNowPlaying() {
		return nowPlaying;
	}

	/**
	 * Queues all given SongStreams
	 * @param songs the SongStreams to be queued
	 */
	public void queue(List<SongStream> songs) {
		for(SongStream stream : songs)
			queue(stream);
	}
}
