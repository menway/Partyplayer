package tests;

import java.io.File;
import java.util.List;

import player.Player;
import songstreams.SongStream;

import library.Library;

public class TestPlayer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Library library = new Library(new File(System.getProperty("user.dir") + "/music"));
			List<SongStream> songs = library.getAllSongs();
			Player player = new Player();
			for(SongStream song : songs)
				player.queue(song);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
