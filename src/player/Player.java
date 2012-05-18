package player;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import library.interfaces.SongStream;

public class Player implements PlayerListener {
	private SongStream nowPlaying;
	private final PlayerExecutor playerExecutor;
	private final List<SongStream> queue;
	private final Stack<SongStream> lastPlayed;
	private boolean isPaused;
	private boolean isStopped;
	
	public Player() {
		playerExecutor = new PlayerExecutor();
		queue = new LinkedList<SongStream>();
		lastPlayed = new Stack<SongStream>();
		isStopped = true;
	}
	
	
	public void queue(SongStream stream) throws UnsupportedEncodingException {
		queue.add(stream);
	}
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
	public void pause() {
		playerExecutor.pause();
		isPaused = true;
	}
	public void stop() {
		playerExecutor.stop();
		isStopped = true;
	}
	public void skip() {
		stop();
		lastPlayed.push(queue.remove(0));
		play();
	}
	public void back() {
		stop();
		queue.add(0, lastPlayed.pop());
		play();
	}
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
	public SongStream getNowPlaying() {
		return nowPlaying;
	}


	public void queue(List<SongStream> selectedSongs) throws UnsupportedEncodingException {
		for(SongStream stream : selectedSongs)
			queue(stream);
	}
}
