package player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Runs PlayerThreads
 * @author Joakim Reinert
 *
 */
public class PlayerExecutor extends ThreadPoolExecutor {
	
	public PlayerExecutor() {
		super(1, 1, 0, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		// TODO Auto-generated constructor stub
	}
	private boolean isPaused;
	private PlayerThread runningThread;
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		runningThread = (PlayerThread) r;
	}
	/**
	 * Pauses playback for the running PlayerThread
	 */
	public void pause() {
		runningThread.pause();
	}
	/**
	 * Stops playback of the running PlayerThread (not resumable)
	 */
	public void stop() {
		runningThread.stop();
		pause();
	}
	/**
	 * Resumes the paused running PlayerThread
	 */
	public void resume() {
		runningThread.resume();
	}
	/**
	 * 
	 * @return <b>true</b> if the currently running PlayerThread is paused, <b>false</b> otherwise
	 */
	public boolean isPaused() {
		return isPaused;
	}
}
