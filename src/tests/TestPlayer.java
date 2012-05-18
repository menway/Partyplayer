package tests;

import java.io.File;
import java.util.List;

import player.Player;

import library.Library;
import library.conditions.TrueCondition;
import library.interfaces.SongStream;

public class TestPlayer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TrueCondition tc = new TrueCondition();
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
