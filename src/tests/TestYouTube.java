package tests;

import java.net.URL;


import player.Player;
import songstreams.SongStream;
import songstreams.YouTubeSongStream;

public class TestYouTube {
	public static void main(String[] args) {
		Player player = new Player();
		try {
			SongStream stream = new YouTubeSongStream(new URL("http://www.youtube.com/watch?v=-9DFWp32L6g"));
			player.queue(stream);
			player.play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
